package strings;

import data_structure.LinkedQueue;

/**
 * The <tt>TST</tt> class represents an symbol table of key-value paris,
 * with string keys and generic values.
 * It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>, <em>delete</em>,
 * <em>size</em>, and <em>is-empty</em> methods.
 * It also provides character-based methods for finding the string in the ST that is
 * the <em>longest prefix</em> of a given prefix, finding all string in the ST that
 * <em>start with</em> a given prefix.
 * A symbol table implements the <em>associative array</em> abstraction:
 * when associating a value with a key that is already in the symbol table,
 * the convention is to replace the old value with the new value.
 * Unlike {@link java.util.Map}, this class uses the convention that
 * values cannot be <tt>null</tt>&mdash;setting the
 * value associated with a key to <tt>null</tt> is equivalent to deleting the key
 * from the symbol table.
 * <p/>
 * This implementation uses a ternary search trie.
 * <p/>
 */
public class TST<Value> {
    private int N;  // size
    private Node root;  // root of TST

    private static class Node<Value> {
        private char c;  // character
        private Object val;  // value associated with string
        private Node left, mid, right;  // left, middle, and right sub-tries
    }

    /**
     * Returns the number of key-value paris in this ST.
     *
     * @return the number of key-value paris in this ST
     */
    public int size() {
        return N;
    }

    /**
     * Inserts the key-value pair into the ST, overwriting the old value with
     * the new value if the key is already in the ST.
     *
     * @param key the key
     * @param val the value
     */
    public void put(String key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new Node();
            x.c = c;
        }
        if (c < x.c) x.left = put(x.left, key, val, d);
        else if (c > x.c) x.right = put(x.right, key, val, d);
        else if (d < key.length() - 1) x.mid = put(x.mid, key, val, d + 1);
        else {
            x.val = val;
            N++;
        }
        return x;
    }

    /**
     * Does this symbol table contain the given key?
     *
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     * <tt>false</tt> otherwise
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     * and <tt>null</tt> if the key is not in the symbol table
     */
    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        else return (Value) x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        char c = key.charAt(d);
        if (c < x.c) return get(x.left, key, d);
        else if (c > x.c) return get(x.right, key, d);
        else if (d < key.length() - 1) return get(x.mid, key, d + 1);
        else return x;
    }

    /**
     * Returns all keys in the symbol table as an <tt>Iterable</tt>.
     *
     * @return all keys in the sybol table as an <tt>Iterable</tt>
     */
    public Iterable<String> keys() {
        LinkedQueue<String> queue = new LinkedQueue<>();
        collect(root, "", queue);
        return queue;
    }

    private void collect(Node x, String prefix, LinkedQueue<String> queue) {
        if (x == null) return;
        collect(x.left, prefix, queue);
        if (x.val != null) queue.enqueue(prefix + x.c);
        collect(x.mid, prefix + x.c, queue);
        collect(x.right, prefix, queue);
    }

    /**
     * Returns all of the keys in the set that start with <tt>prefix</tt>.
     *
     * @param prefix the prefix
     * @return all of the keys in the set that start with <tt>prefix</tt>,
     * as an iterable
     */

    public Iterable<String> keysWithPrefix(String prefix) {
        LinkedQueue<String> queue = new LinkedQueue<>();
        Node x = get(root, prefix, 0);
        if (x.val != null) queue.enqueue(prefix);
        collect(x.mid, prefix, queue);
        return queue;
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of <tt>query</tt>,
     * or <tt>null</tt>, if no such string.
     *
     * @param query the query string
     * @return the string in the symbol table that is the longest prefix of <tt>query</tt>,
     * or <tt>null</tt> if no such string
     */
    public String longestPrefixOf(String query) {
        int length = search(root, query, 0, 0);
        return query.substring(0, length);
    }

    private int search(Node x, String query, int d, int l) {
        if (x == null) return l;
        if (x.val != null) l = d;
        if (d == query.length()) return l;
        char c = query.charAt(d);
        if (c < x.c) return search(x.left, query, d, l);
        else if (c > x.c) return search(x.right, query, d, l);
        else return search(x.mid, query, d + 1, l);
    }

    public static void main(String[] args) {

        // build symbol table
        TST<Integer> tst = new TST<>();
        tst.put("by", 4);
        tst.put("sea", 6);
        tst.put("sells", 1);
        tst.put("she", 0);
        tst.put("shells", 3);
        tst.put("shore", 7);
        tst.put("the", 5);

        // print keys
        System.out.print(tst.size() + " keys:  ");
        for (String key : tst.keys()) System.out.print(key + "->" + tst.get(key) + " ");
        System.out.println();

        System.out.println("longest prefix of \"shellsort\": " + tst.longestPrefixOf("shellsort"));
        System.out.println("keys with prefix \"shor\":" + tst.keysWithPrefix("shor"));
    }
}