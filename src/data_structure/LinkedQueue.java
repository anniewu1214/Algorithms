package data_structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

// Implement a Queue using singly-linked list.
public class LinkedQueue<T> implements Iterable<T> {
    private int N; // number of elements on queue
    private Node<T> first;  // beginning of queue
    private Node<T> last;  // end of queue

    // helper linked list class
    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    // Initializes an empty queue
    public LinkedQueue() {
        first = null;
        last = null;
        N = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException();
        return first.data;
    }

    public void enqueue(T data) {
        Node<T> oldLast = last;
        last = new Node<T>(data, null);
        if (isEmpty()) first = last;
        else oldLast.next = last;
        N++;
    }

    public T dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        T data = first.data;
        first = first.next;
        N--;
        if (isEmpty()) last = null;
        return data;
    }

    public int size() {
        return N;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T data : this)
            sb.append(data).append(" ");
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> nextNode = first;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public T next() {
                T data = nextNode.data;
                nextNode = nextNode.next;
                return data;
            }
        };
    }

    public static void main(String[] args) {
        LinkedQueue<String> q = new LinkedQueue<String>();
        q.enqueue("1");
        q.enqueue("2");
        q.enqueue("3");
        System.out.println(q);
        q.dequeue();
        System.out.println(q);
        q.dequeue();
        q.dequeue();
        System.out.println(q);
    }
}