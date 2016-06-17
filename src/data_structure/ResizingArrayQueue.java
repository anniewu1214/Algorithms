package data_structure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Queue implementation with a resizing array.
 */
public class ResizingArrayQueue<T> implements Iterable<T> {
    private T[] q;  // queue elements
    private int N = 0; // number of elements on queue
    private int first = 0;  // index of first element
    private int last = 0;  // index of next available slot

    public ResizingArrayQueue() {
        // cast needed since no generic array creation in Java
        q = (T[]) new Object[2];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void enqueue(T t) {
        // double size of array if necessary and recopy to front of array
        if (N == q.length) resize(2 * q.length);
        q[last++] = t; // add item
        if (last == q.length) last = 0; // wrap-around
        N++;
    }

    private void resize(int capacity) {
        assert capacity >= N;
        T[] temp = (T[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = q[(first + i) % q.length];
        }
        q = temp;
        first = 0;
        last = N;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        T firstElement = q[first];
        q[first] = null;  // to avoid loitering
        first++;
        N--;
        if (first == q.length) first = 0; // wrap-around
        if (N > 0 && N == q.length / 4) resize(q.length / 2);
        return firstElement;
    }


    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        return q[first];
    }

    @Override
    public String toString() {
        return Arrays.toString(q);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < N;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T t = q[(i + first) % q.length];
                i++;
                return t;
            }
        };
    }

    public static void main(String[] args) {
        ResizingArrayQueue<String> q = new ResizingArrayQueue<String>();
        q.enqueue("to");
        q.enqueue("be");
        q.enqueue("or");
        System.out.println(q);
        q.enqueue("not");
        q.enqueue("to");
        q.enqueue("be");
        System.out.println(q);

        q.dequeue();
        q.dequeue();
        q.dequeue();
        q.dequeue();
        System.out.println(q);
        q.dequeue();
        System.out.println(q);
    }
}