package strings;

import data_structure.LinkedQueue;

/**
 * The <tt>TrieST</tt> class represents an symbol table of key-value
 * paris, with string keys and generic values.
 * It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 * <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 * It also provides character-based methods for finding the string in
 * the symbol table that is the <em>longest prefix</em> of a given prefix,
 * finding all strings in the symbol table that <em>start with</em> a given prefix.
 * A symbol table implements the <em>associative array</em> abstraction:
 * when associating a value with a key that is already in the symbol table, the
 * convention is to replace the old value with the new value.
 * Unlike {@link java.util.Map}, this class uses the convention that values
 * cannot be <tt>null</tt>&mdash;setting the value associated with a key to
 * <tt>null</tt> is equivalent to delete the key from the symbol table.
 * <p>
 * This implementation uses a 256-way trie.
 * The <em>put</em>, <em>contains</em>, <em>delete</em>, and <em>longest prefix</em>
 * operations take time proportional to the length of the key (in the worst case).
 * Construction takes constant time. The <em>size</em>, and <em>is-empty</em> operations take constant time.
 * </p>
 */
public class TrieST<Value> {

    private static final int R = 256;  // extended ASCII
    private Node root = new Node();  // root of trie
    private int N;  // number of keys in trie

    // R-way trie node
    private static class Node {
        private Object value;
        private Node[] next = new Node[R];
    }


    /**
     * Insert the key-value pair into the symbol table, overwriting the old  value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the table.
     *
     * @param key the key
     * @param val the value
     */
    public void put(String key, Value val) {
        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (x.value == null) N++;
            x.value = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d + 1);
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
        else return (Value) x.value;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return N;
    }

    /**
     * Is this symbol table empty?
     *
     * @return <tt>true</tt> if this symbol table is empty and <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return N == 0;
    }

    /**
     * Removes the key from the set if the key is present.
     *
     * @param key the key
     */

    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.value != null) N--;
            x.value = null;
        } else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // remove sub-trie rooted at x if it is completely empty
        if (x.value != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }


    /**
     * Returns all keys in the symbol table as an <tt>Iterable</tt>.
     * To iterate over all of the keys in the symbol table named <tt>st</tt>,
     * use the foreach notation: <tt>for (Key key : st.keys())</tt>.
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
        if (x.value != null) queue.enqueue(prefix);
        for (int c = 0; c < R; c++) {
            collect(x.next[c], prefix + (char) c, queue);
        }
    }

    /**
     * Returns all of the keys in the symbol table that start with <tt>prefix</tt>.
     *
     * @param prefix the prefix
     * @return all keys in the ST that start with <tt>prefix</tt>, as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        LinkedQueue<String> queue = new LinkedQueue<>();
        Node x = get(root, prefix, 0);
        collect(x, prefix, queue);
        return queue;
    }

    /**
     * Returns the string in the ST that is the longest prefix of <tt>query</tt> or
     * <tt>null</tt>, if no such string.
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
        if (x.value != null) l = d;
        if (d == query.length()) return l;
        char c = query.charAt(d);
        return search(x.next[c], query, d + 1, l);
    }

    public static void main(String[] args) {

        // build symbol table
        TrieST<Integer> st = new TrieST<>();
        st.put("by", 4);
        st.put("sea", 6);
        st.put("sells", 1);
        st.put("she", 0);
        st.put("shells", 3);
        st.put("shore", 7);
        st.put("the", 5);
        // delete
        st.delete("she");

        // print keys
        System.out.print(st.size() + " keys:  ");
        for (String key : st.keys()) System.out.print(key + "->" + st.get(key) + " ");
        System.out.println();


        System.out.println("longest prefix of \"shellsort\": " + st.longestPrefixOf("shellsort"));
        System.out.println("keys with prefix \"sh\":" + st.keysWithPrefix("sh"));
    }
}