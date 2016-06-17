package search_sort;

import helper.*;
import data_structure.*;

/**
 * Symbol table implementation with binary search in an ordered array.
 */
public class BinarySearchST<Key extends Comparable<Key>, Value> {
    public static final int INIT_CAPACITY = 2;
    private Key[] keys;
    private Value[] vals;
    private int N;

    // create an empty symbol table with default initial capacity
    public BinarySearchST() {
        this(INIT_CAPACITY);
    }

    // create an empty symbol table with given initial capacity
    public BinarySearchST(int capacity) {
        keys = (Key[]) new Comparable[capacity];
        vals = (Value[]) new Object[capacity];
    }

    // resize the underlying arrays
    private void resize(int capacity) {
        assert capacity >= N;
        Key[] tempk = (Key[]) new Comparable[capacity];
        Value[] tempv = (Value[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            tempk[i] = keys[i];
            tempv[i] = vals[i];
        }
        vals = tempv;
        keys = tempk;
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

    // return the value associated with the given key, or null if no such key
    public Value get(Key key) {
        if (isEmpty()) return null;
        int i = rank(key);
        if (i < N && keys[i].compareTo(key) == 0) return vals[i]; // if key exists
        return null;
    }

    // return the number of keys in the table that are smaller than given key
    private int rank(Key key) {
        int lo = 0, hi = N - 1;
        while (lo <= hi) {
            int m = lo + (hi - lo) / 2;
            int cmp = key.compareTo(keys[m]);
            if (cmp < 0) hi = m - 1;
            else if (cmp > 0) lo = m + 1;
            else return m;
        }
        return lo;
    }

    // Search for key. Update value if found; grow table if new.
    public void put(Key key, Value val) {
        // if value is null, delete key
        if (val == null) {
            delete(key);
            return;
        }

        int i = rank(key);

        // key is already in table
        if (i < N && keys[i].compareTo(key) == 0) {
            vals[i] = val;
            return;
        }

        // key not in table, insert new key-value pair
        if (N == keys.length) resize(2 * N);
        for (int j = N; j > i; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }
        keys[i] = key;
        vals[i] = val;
        N++;
    }

    // remove key-value pair if present
    private void delete(Key key) {
        if (isEmpty()) return;
        int i = rank(key); // compute rank
        if (i == N || keys[i].compareTo(key) != 0) return; // key not in table
        for (int j = i; j < N - 1; j++) {
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }
        N--;
        keys[N] = null; // to avoid loitering
        vals[N] = null;

        // resize if 1/4 full
        if (N > 0 && N == keys.length / 4) resize(keys.length / 2);
    }

    /*****************************************************************************
     * Ordered symbol table methods
     *****************************************************************************/
    // smallest key
    public Key min() {
        if (isEmpty()) return null;
        return keys[0];
    }

    // largest key
    public Key max() {
        if (isEmpty()) return null;
        return keys[N - 1];
    }

    // key of rank k
    public Key select(int k) {
        if (k < 0 || k >= N) return null;
        return keys[k];
    }

    // the largest key that is less than or equal to the given key
    public Key floor(Key key) {
        int i = rank(key);
        if (i < N && key.compareTo(keys[i]) == 0) return keys[i];
        if (i == 0) return null;
        else return keys[i - 1];
    }

    // the smallest key that is greater than or equal to the given key
    public Key ceiling(Key key) {
        int i = rank(key);
        if (i == N) return null;
        else return keys[i];
    }

    // number of keys in [lo .. hi]
    public int size(Key lo, Key hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    // return all keys in table, in sorted order
    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    // keys in [lo .. hi], in sorted order
    public Iterable<Key> keys(Key lo, Key hi) {
        LinkedQueue<Key> queue = new LinkedQueue<Key>();
        if (lo == null && hi == null) return queue;
        if (lo == null) throw new NullPointerException("lo is null in keys()");
        if (hi == null) throw new NullPointerException("hi is null in keys()");
        if (lo.compareTo(hi) > 0) return queue;
        for (int i = rank(lo); i < rank(hi); i++)
            queue.enqueue(keys[i]);
        if (contains(hi)) queue.enqueue(keys[rank(hi)]);
        return queue;
    }

    public static void main(String[] args) {
        BinarySearchST<String, Integer> st = new BinarySearchST<String, Integer>();
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