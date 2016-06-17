package data_structure;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ILinkedList<T> implements Iterable<T> {

    private Node<T> head; // the linked list

    // Create an empty linked list
    public ILinkedList() {
        head = null;
    }

    // Is the list empty
    public boolean isEmpty() {
        return head == null;
    }

    // Insert a new node at the beginning of list
    public void addFirst(T item) {
        head = new Node<T>(item, head);
    }

    // Get the first element of the list
    public T getFirst() {
        if (head == null) throw new NoSuchElementException("The list is empty");
        return head.data;
    }

    // Remove the first element from the list
    public T removeFirst() {
        T tmp = getFirst();
        head = head.next;
        return tmp;
    }

    // Insert a new node at the end of the list
    public void addLast(T item) {
        if (head == null) addFirst(item);
        else {
            Node<T> tmp = head;
            while (tmp.next != null) tmp = tmp.next;
            tmp.next = new Node<T>(item, null);
        }
    }

    public Node<T> addLastRecursive(Node<T> head, T item) {
        if (head == null) head = new Node<T>(item, null);
        if (head.next == null) head.next = new Node<T>(item, null);
        else head.next = addLastRecursive(head.next, item);
        return head;
    }

    public void addLastRecursive(T item) {
        this.head = addLastRecursive(this.head, item);
    }

    // Get the last element of the list
    public T getLast() {
        if (head == null) throw new NoSuchElementException();
        Node<T> tmp = head;
        while (tmp.next != null) tmp = tmp.next;
        return tmp.data;
    }

    public T getLastRecursive(Node<T> head) {
        if (head == null) throw new NoSuchElementException();
        if (head.next == null) return head.data;
        else return getLastRecursive(head.next);
    }

    public T getLastRecursive() {
        return getLastRecursive(this.head);
    }

    // Remove all nodes from the list
    public void clear() {
        head = null;
    }

    // Get the length of list
    public int length() {
        int len = 0;
        for (T data : this) len++;
        return len;
    }

    public int lengthRecursive(Node<T> head) {
        if (head == null) return 0;
        else return 1 + lengthRecursive(head.next);
    }

    public int lengthRecursive() {
        return lengthRecursive(this.head);
    }

    // Does the list contains an element
    public boolean contains(T x) {
        for (T data : this) if (data.equals(x)) return true;
        return false;
    }

    // Get the data at the specified position
    public T get(int pos) {
        Node<T> tmp = head;
        for (int i = 0; i < pos; i++) {
            if (tmp == null) throw new IndexOutOfBoundsException();
            tmp = tmp.next;
        }
        return tmp.data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T x : this) sb.append(x.toString()).append(" ");
        return sb.toString();
    }

    public void print(Node<T> head) {
        if (head == null) return;
        System.out.println(head.data);
        print(head.next);
    }

    public void reversePrint(Node<T> head) {
        if (head == null) return;
        reversePrint(head.next);
        System.out.println(head.data);
    }

    // Insert a new node after the node containing the key
    public void insertAfter(T key, T toInsert) {
        Node<T> tmp = head;
        while (tmp != null && !tmp.data.equals(key)) tmp = tmp.next;
        if (tmp != null) tmp.next = new Node<T>(toInsert, tmp.next);
    }

    public Node<T> insertAfterRecursive(Node<T> head, T key, T toInsert) {
        if (head == null) return null;
        if (head.data == key) head.next = new Node<T>(toInsert, head.next);
        else head.next = insertAfterRecursive(head.next, key, toInsert);

        return head;
    }

    public void insertAfterRecursive(T key, T toInsert) {
        this.head = insertAfterRecursive(head, key, toInsert);
    }


    // Insert a new node before the node containing the key
    public void insertBefore(T key, T toInsert) {
        if (head == null) return;

        Node<T> previous = null;
        Node<T> current = head;

        while (current != null && !current.data.equals(key)) {
            previous = current;
            current = current.next;
        }

        if (previous == null) addFirst(toInsert);
        else previous.next = new Node<T>(toInsert, current);
    }

    public Node<T> insertBeforeRecursive(Node<T> head, T key, T toInsert) {
        if (head == null) return null;
        if (head.data == key) head = new Node<T>(toInsert, head);
        else head.next = insertBeforeRecursive(head.next, key, toInsert);
        return head;
    }

    public void insertBeforeRecursive(T key, T toInsert) {
        this.head = insertBeforeRecursive(head, key, toInsert);
    }

    // Remove the first occurrence of the specified element
    public void remove(T value) {
        if (head == null) throw new RuntimeException("cannot delete");

        Node<T> previous = null;
        Node<T> current = head;

        while (current != null && !current.data.equals(value)) {
            previous = current;
            current = current.next;
        }

        if (previous == null) head = head.next;
        else if (current != null) previous.next = current.next;
    }

    public Node<T> removeRecursive(Node<T> head, T value) {
        if (head == null) return null;
        if (head.data == value) return head.next;
        else {
            head.next = removeRecursive(head.next, value);
            return head;
        }
    }

    public void removeRecursive(T value) {
        this.head = removeRecursive(head, value);
    }

    public Node<T> removeAll(Node<T> head, T value) {
        if (head == null) return null;
        if (head.data == value) return removeAll(head.next, value);
        else {
            head.next = removeAll(head.next, value);
            return head;
        }
    }

    public void removeAll(T value) {
        this.head = removeAll(head, value);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> nextNode = head;

            @Override
            public boolean hasNext() {
                return nextNode != null;
            }

            @Override
            public T next() {
                T res = nextNode.data;
                nextNode = nextNode.next;
                return res;
            }
        };
    }

    // Return a deep copy of list, complexity: O(n^2)
    public ILinkedList<T> copy1() {
        ILinkedList<T> twin = new ILinkedList<T>();
        for (T data : this) {
            twin.addLast(data);
        }
        return twin;
    }

    // Return a deep copy of list, complexity: O(n)
    public ILinkedList<T> copy2() {
        ILinkedList<T> twin = new ILinkedList<T>();
        for (T data : this) {
            twin.addFirst(data);
        }
        return twin.reverse();
    }

    // Reverse the list
    private ILinkedList<T> reverse() {
        ILinkedList<T> list = new ILinkedList<T>();
        for (T node : this) {
            list.addFirst(node);
        }
        return list;
    }

    // Reverse a linked list using recursion
    private Node<T> reverseRecursive(Node<T> list) {

        if (list == null) return null; // first question
        if (list.next == null) return list; // second question

        // third question - in Lisp this is easy, but we don't have cons
        // so we grab the second element (which will be the last after we reverse it)
        Node<T> secondElem = list.next;

        // bug fix - need to unlink list from the rest or you will get a cycle
        list.next = null;

        // then we reverse everything from the second element on
        Node<T> reverseRest = reverseRecursive(secondElem);

        // then we join the two lists
        secondElem.next = list;

        return reverseRest;
    }

    // The node class.
    private static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data, Node<T> next) {
            this.data = data;
            this.next = next;
        }
    }

    private void reverseRecursive() {
        this.head = reverseRecursive(head);
    }

    public static void main(String[] args) {
        ILinkedList<String> list = new ILinkedList<String>();
        list.addFirst("a");
        list.addFirst("b");
        list.addFirst("c");
        list.addFirst("d");
        System.out.println("After AddFirst a, b, c, d: " + list);

        ILinkedList<String> twin = list.copy2();
        System.out.println("Deep copy result: " + twin);
        System.out.println("the first element of list: " + list.get(0));

        // System.out.println(list.get(4));   //exception
        list.addLast("s");
        Iterator iterator = list.iterator();
        while (iterator.hasNext())
            System.out.print(iterator.next() + " ");

        System.out.println(" is the result after addLast s.");

        list.insertAfter("a", "aa");
        System.out.println("After inserting aa after a: " + list);

        list.insertBefore("d", "ee");
        System.out.println("After inserting ee before d: " + list);

        list.remove("ee");
        System.out.println("After removing ee: " + list);

        list.addLastRecursive("z");
        System.out.println(list);

        System.out.println(list.getLastRecursive());

        list.insertAfterRecursive("z", "zz");
        list.insertBeforeRecursive("d", "d");
        System.out.println(list);

        System.out.println(list.lengthRecursive());
        list.removeRecursive("z");
        System.out.println(list);
        list.removeAll("d");
        System.out.println(list);
        list.reverseRecursive();
        System.out.println(list);
    }
}