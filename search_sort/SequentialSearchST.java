package search_sort;

import data_structure.LinkedQueue;

/**
 * Symbol table implementation with sequential search in an unordered linked list of key-value paris.
 */
public class SequentialSearchST<Key, Value> {
    private int N; // number of key-value paris
    private Node first; // the linked list of key-value paris

    // a helper linked list data type
    private class Node {
        private Key key;
        private Value val;
        private Node next;

        public Node(Key key, Value val, Node next) {
            this.key = key;
            this.val = val;
            this.next = next;
        }
    }

    public SequentialSearchST() {
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key
     */
    public Value get(Key key) {
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) return x.val;
        }
        return null;
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value with
     * the new value if the key is already in the symbol table.
     */
    public void put(Key key, Value val) {
        if (val == null) {
            delete(key);
            return;
        }
        for (Node x = first; x != null; x = x.next) {
            if (key.equals(x.key)) {
                x.val = val;
                return;
            }
        }
        first = new Node(key, val, first);
        N++;
    }

    /**
     * Remove the key and associated value from the symbol table.
     */
    public void delete(Key key) {
        first = delete(first, key);
    }

    // delete key in linked list beginning at Node x
    private Node delete(Node x, Key key) {
        if (x == null) return null;
        if (key.equals(x.key)) {
            N--;
            return x.next;
        }
        x.next = delete(x.next, key);
        return x;
    }

    /**
     * Returns all keys in the symbol table as an Iterable.
     */
    public Iterable<Key> keys() {
        LinkedQueue<Key> queue = new LinkedQueue<Key>();
        for (Node x = first; x != null; x = x.next) queue.enqueue(x.key);
        return queue;
    }

    public static void main(String[] args) {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<String, Integer>();
        st.put("L", 11);
        st.put("P", 10);
        st.put("M", 9);
        st.put("X", 7);
        st.put("H", 5);
        st.put("C", 4);
        st.put("R", 3);
        st.put("A", 8);
        st.put("E", 12);
        st.put("S", 0);
        st.delete("L");
        for (String s : st.keys()) {
            System.out.println(s + " " + st.get(s));
        }
    }
}