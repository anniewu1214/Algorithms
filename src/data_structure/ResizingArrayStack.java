package data_structure;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Stack implementation with a resizing array.
 */
public class ResizingArrayStack<T> implements Iterable<T>{
    private T[] a; // array of items
    private int N; // number of elements on stack

    public ResizingArrayStack() {
        a = (T[]) new Object[2];
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void push(T t) {
        if (N == a.length) resize(2 * a.length); // double the size of array if necessary
        a[N++] = t; // add item
    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= N;
        T[] temp = (T[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = a[i];
        }
        a = temp;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        T t = a[N - 1];
        a[N - 1] = null; // for gc, avoiding loitering
        N--;
        // shrink size of array if necessary
        if (N > 0 && N == a.length / 4) resize(a.length / 2);
        return t;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return a[N - 1];
    }

    @Override
    public String toString() {
        return Arrays.toString(a);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int i = N - 1;
            @Override
            public boolean hasNext() {
                return i >= 0;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return a[i--];
            }
        };
    }

    public static void main(String[] args) {
        ResizingArrayStack<String> s = new ResizingArrayStack<String>();
        s.push("to");
        System.out.println(s);
        s.push("be");
        System.out.println(s);
        s.push("or");
        System.out.println(s);
        s.push("not");
        System.out.println(s);
        s.push("to");
        System.out.println(s);
        s.push("be");
        System.out.println(s);
        s.pop();
        s.pop();
        s.pop();
        System.out.println(s);
        s.pop();
        System.out.println(s);
    }
}