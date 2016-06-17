package search_sort;

import data_structure.LinkedQueue;

/**
 * Symbol table implementation with linear probing hash table
 */
public class LinearProbingHashST<Key, Value> {
    public static final int INIT_CAPACITY = 4;

    private int N; // number of key-value pairs int the symbol table
    private int M; // size of linear probing table
    private Key[] keys;
    private Value[] vals;

    public LinearProbingHashST() {
        this(INIT_CAPACITY);
    }

    public LinearProbingHashST(int capacity) {
        M = capacity;
        keys = (Key[]) new Object[M];
        vals = (Value[]) new Object[M];
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }

    // hash function for keys - returns value between 0 and M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    // return the value associated with the given key, null if no such value
    public Value get(Key key) {
        for (int i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) return vals[i];
        }
        return null;
    }

    // insert the key-value pair into the symbol table
    public void put(Key key, Value val) {
        if (val == null) {
            delete(key);
            return;
        }

        // double table size if 50% full
        if (N >= M / 2) resize(2 * M);

        int i;
        for (i = hash(key); keys[i] != null; i = (i + 1) % M) {
            if (keys[i].equals(key)) {
                vals[i] = val;
                return;
            }
        }
        keys[i] = key;
        vals[i] = val;
        N++;
    }

    // delete the key and associated value from the symbol table
    private void delete(Key key) {
        if (!contains(key)) return;
        // find position i of key
        int i = hash(key);
        while (!key.equals(keys[i])) {
            i = (i + 1) % M;
        }

        // delete key and associated value
        keys[i] = null;
        vals[i] = null;
        N--;

        // rehash all keys in same cluster
        i = (i + 1) % M;
        while (keys[i] != null) {
            // delete keys[i] and vals[i] and reinsert
            Key keyToRehash = keys[i];
            Value valToRehash = vals[i];
            keys[i] = null;
            vals[i] = null;
            N--;
            put(keyToRehash, valToRehash);
            i = (i + 1) % M;
        }

        // halve size of array if it's 12.5% full or less
        if (N > 0 && N <= M / 8) resize(M / 2);
    }

    private void resize(int capacity) {
        LinearProbingHashST<Key, Value> temp = new LinearProbingHashST<Key, Value>(capacity);
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) {
                temp.put(keys[i], vals[i]);
            }
        }
        keys = temp.keys;
        vals = temp.vals;
        M = temp.M;
    }

    public Iterable<Key> keys() {
        LinkedQueue queue = new LinkedQueue();
        for (int i = 0; i < M; i++) {
            if (keys[i] != null) queue.enqueue(keys[i]);
        }
        return queue;
    }
    public static void main(String[] args) {
        LinearProbingHashST<String, Integer> st = new LinearProbingHashST<String, Integer>();
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