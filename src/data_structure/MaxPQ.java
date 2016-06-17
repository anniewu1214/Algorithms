package data_structure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Generic max priority queue implementation with a binary heap.
 * Can be used with a comparator instead of the natural order, but the
 * generic key type must still be comparable
 */
public class MaxPQ<Key> implements Iterable<Key> {

    private Key[] pq; // store items at indices 1 to N
    private int N; // number of items on priority queue
    private Comparator<Key> comparator; // optional Comparator

    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param initCapacity the initial capacity of the priority queue
     */
    public MaxPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    /**
     * Initializes an empty priority queue.
     */
    public MaxPQ() {
        this(1);
    }

    /**
     * Initializes an empty priority queue with the given initial capacity,
     * using the given comparator.
     *
     * @param initCapacity the initial capacity of the priority queue
     * @param comparator   the order in which to compare the keys
     */
    public MaxPQ(int initCapacity, Comparator<Key> comparator) {
        this.comparator = comparator;
        pq = (Key[]) new Object[initCapacity + 1];
        N = 0;
    }

    /**
     * Initializes an empty priority queue using the given comparator.
     *
     * @param comparator the order in which to compare the keys
     */
    public MaxPQ(Comparator<Key> comparator) {
        this(1, comparator);
    }

    /**
     * Initializes a priority queue from the array of keys.
     * Taken time proportional to the number of keys, using sink based heap construction.
     *
     * @param keys the array of keys
     */
    //TODO
    public MaxPQ(Key[] keys) {
        N = keys.length;
        pq = (Key[]) new Object[keys.length + 1];
        for (int i = 0; i < N; i++) pq[i + 1] = keys[i];

    }

    /**
     * Is the priority queue empty?
     *
     * @return true if the priority queue is empty; false otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Return the number of keys on the priority queue.
     *
     * @return the number of keys on the priority queue.
     */
    public int size() {
        return N;
    }

    /**
     * Returns a largest key on the priority queue.
     *
     * @return a largest key on the priority queue
     * @throws java.util.NoSuchElementException if the priority queue is empty
     */
    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    /**
     * Adds a new key to the priority queue.
     *
     * @param x the new key to add to the priority queue
     */
    public void insert(Key x) {
        // double size of array if necessary
        if (N >= pq.length - 1) resize(2 * pq.length);

        // add x, and percolate it up to maintain heap invariant
        pq[++N] = x;
        swim(N);
    }

    // helper function to double the size of the heep array
    private void resize(int capacity) {
        assert capacity > N;
        Key[] temp = (Key[]) new Object[capacity];
        System.arraycopy(pq, 1, temp, 1, N);
        pq = temp;
    }

    /**
     * Remove and returns a largest key on the priority queue.
     *
     * @return a largest key on the priority queue
     * @throws java.util.NoSuchElementException if priority queue is empty
     */
    public Key delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        Key max = pq[1];
        exch(1, N--);
        sink(1);
        pq[N + 1] = null; // to avoid loitering and help with garbage collection
        if ((N > 0) && N == (pq.length - 1) / 4) resize(pq.length / 2); // resize array if necessary
        return max;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Key key : this) {
            sb.append(key).append(" ");
        }
        return sb.toString();
    }

    /***********************************************************************
     * Helper functions to restore the heap invariant.
     **********************************************************************/
    private void swim(int k) {
        while (k > 1 && less(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && less(j, j + 1)) j++;
            if (!less(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    /***********************************************************************
     * Helper functions for compares and swaps.
     **********************************************************************/
    private boolean less(int i, int j) {
        if (comparator == null) {
            return ((Comparable<Key>) pq[i]).compareTo(pq[j]) < 0;
        } else {
            return comparator.compare(pq[i], pq[j]) < 0;
        }
    }

    private void exch(int i, int j) {
        Key swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    /***********************************************************************
     * Iterator
     **********************************************************************/

    @Override
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        // create a new pq
        private MaxPQ<Key> copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new MaxPQ<Key>(size());
            else copy = new MaxPQ<Key>(size(), comparator);
            for (int i = 1; i <= N; i++) copy.insert(pq[i]);
        }

        @Override
        public boolean hasNext() {
            return !copy.isEmpty();
        }

        @Override
        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }
    }

    public static void main(String[] args) {
        MaxPQ<String> pq = new MaxPQ<String>();
        pq.insert("P");
        pq.insert("Q");
        pq.insert("E");
        System.out.println(pq);
        pq.delMax();
        pq.insert("X");
        pq.insert("A");
        pq.insert("M");
        pq.delMax();
        System.out.println(pq);
        pq.insert("P");
        pq.insert("L");
        pq.insert("E");
        pq.delMax();
        System.out.println(pq);
    }
}