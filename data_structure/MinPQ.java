package data_structure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generic min priority queue implementation with a binary heap.
 * Can be used with a comparator instead of the natural order.
 */

public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq;  // store items at indices 1 to N
    private int N;  // number of items on priority queue
    private Comparator<Key> comparator; // optional comparator

    // Initializes an empty priority queue with the given initial capacity.
    public MinPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    public MinPQ() {
        this(1);
    }

    public MinPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    public MinPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    public MinPQ(Key[] keys) {
        N = keys.length;
        pq = (Key[]) new Object[keys.length + 1];
        System.arraycopy(keys, 0, pq, 1, N);
        for (int k = N / 2; k >= 1; k--) sink(k);
    }


    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    // returns a smallest key on the priority queue
    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    private void resize(int capacity) {
        assert capacity >= N;
        Key[] temp = (Key[]) new Object[capacity];
        System.arraycopy(pq, 1, temp, 1, N);
        pq = temp;
    }

    // Adds a key to the priority queue.
    public void insert(Key x) {
        if (N == pq.length - 1) resize(2 * pq.length);  // double size of array if necessary
        pq[++N] = x;
        swim(N);
    }

    // Removes and returns a smallest key on the priority queue.
    public Key delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        exch(1, N);
        Key min = pq[N--];
        sink(1);
        pq[N + 1] = null;  // avoid loitering and help with garbage collection
        if ((N > 0) && (N == (pq.length - 1) / 4)) resize(pq.length / 2);
        return min;
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j) {
        if (comparator == null) return ((Comparable<Key>) pq[i]).compareTo(pq[j]) > 0;
        else return comparator.compare(pq[i], pq[j]) > 0;
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Key key: this) {
            sb.append(key).append(" ");
        }
        return sb.toString();
    }

    @Override
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        private int i = 1;

        @Override
        public boolean hasNext() {
            return i <= N;
        }

        @Override
        public Key next() {
            return pq[i++];
        }
    }

    public static void main(String[] args) {
        MinPQ<String> pq = new MinPQ<String>();
        pq.insert("P");
        pq.insert("Q");
        pq.insert("E");
        System.out.println(pq);
        pq.delMin();
        pq.insert("X");
        pq.insert("A");
        pq.insert("M");
        pq.delMin();
        System.out.println(pq);
        pq.insert("P");
        pq.insert("L");
        pq.insert("E");
        pq.delMin();
        System.out.println(pq);
    }
}