package search_sort;

import data_structure.LinkedQueue;

/**
 * A symbol table implemented with a separate-chaining hash table.
 */
public class SeparateChainingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;

    private int N; // number of key-value paris
    private int M; // hash table size
    private SequentialSearchST<Key, Value>[] st; // array of linked-list symbol tables

    public SeparateChainingHashST(int M) {
        this.M = M;
        st = (SequentialSearchST<Key, Value>[]) (new SequentialSearchST[M]);
        for (int i = 0; i < M; i++)
            st[i] = new SequentialSearchST<Key, Value>();
    }

    public SeparateChainingHashST() {
        this(INIT_CAPACITY);
    }

    // hash value between 0 and M-1
    private int hash(Key key) {
        return (key.hashCode() & 0x7fffffff) % M;
    }

    public int size() {
        return N;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    // return value associated with key, null if no such key
    public Value get(Key key) {
        int i = hash(key);
        return st[i].get(key);
    }

    // insert key-value pair into the table
    public void put(Key key, Value val) {
        if (val == null) {
            delete(key);
            return;
        }

        // double table size if average length of list >= 10
        if (N >= 10 * M) resize(2 * M);

        int i = hash(key);
        if (!st[i].contains(key)) N++;
        st[i].put(key, val);
    }

    private void delete(Key key) {
        int i = hash(key);
        if (st[i].contains(key)) N--;
        st[i].delete(key);

        // halve table size if average length of list <= 2
        if (M > INIT_CAPACITY && N <= 2 * M) resize(M / 2);
    }

    // resize the hash table to have the given number of chains b rehashing all of the keys
    private void resize(int chains) {
        SeparateChainingHashST<Key, Value> temp = new SeparateChainingHashST<Key, Value>(chains);
        for (int i = 0; i < M; i++) {
            for (Key key : st[i].keys()) {
                temp.put(key, st[i].get(key));
            }
        }
        this.M = temp.M;
        this.N = temp.N;
        this.st = temp.st;
    }

    // return keys in symbol table as an Iterable
    public Iterable<Key> keys(){
        LinkedQueue<Key> queue = new LinkedQueue<Key>();
        for (int i = 0; i < M; i++) {
            for (Key key: st[i].keys()) {
                queue.enqueue(key);
            }
        }
        return queue;
    }

    public static void main(String[] args) {
        SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<String, Integer>();
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