package search_sort;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

import data_structure.*;

/**
 * A symbol table implementation with a binary search tree
 */
public class BST<Key extends Comparable<Key>, Value> {
    private Node root;  // root of search_sort.BST

    private class Node {
        private Key key;  // sorted by key
        private Value val;  // associated data
        private Node left, right;  // left and right subtree
        private int N;  // number of nodes in subtree

        public Node(Key key, Value val, int n) {
            this.key = key;
            this.val = val;
            N = n;
        }
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return number of key-value paris in search_sort.BST
    public int size() {
        return size(root);
    }

    // return number of key-value pairs in search_sort.BST rooted at x
    private int size(Node x) {
        if (x == null) return 0;
        else return x.N;
    }

    /***********************************************************************
     * Search search_sort.BST for given key, and return associated value if found,
     * return null if not found
     ***********************************************************************/
    public boolean contains(Key key) {
        return get(key) != null;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else return x.val;
    }

    /***********************************************************************
     * Insert key-value pair into search_sort.BST
     * If key already exists, update with new value
     ***********************************************************************/
    public void put(Key key, Value val) {
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
    }

    private Node put(Node x, Key key, Value val) {
        if (x == null) return new Node(key, val, 1);
        int comp = key.compareTo(x.key);
        if (comp < 0) x.left = put(x.left, key, val);
        else if (comp > 0) x.right = put(x.right, key, val);
        else x.val = val;
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }

    /***********************************************************************
     * Delete
     ***********************************************************************/
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMin(root);
    }

    private Node deleteMin(Node x) {
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("Symbol table underflow");
        root = deleteMax(root);
    }

    private Node deleteMax(Node x) {
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }


    public void delete(Key key) {
        root = delete(root, key);
    }

    private Node delete(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = delete(x.left, key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else {
            if (x.right == null) return x.left;
            if (x.left == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }

    /***********************************************************************
     * Min, max, floor, and ceiling
     ***********************************************************************/
    public Key min() {
        if (isEmpty()) return null;
        return min(root).key;
    }

    private Node min(Node x) {
        if (x.left == null) return x;
        else return min(x.left);
    }

    public Key max() {
        if (isEmpty()) return null;
        return max(root).key;
    }

    private Node max(Node x) {
        if (x.right == null) return x;
        else return max(x.right);
    }

    public Key floor(Key key) {
        Node x = floor(root, key);
        if (x == null) return null;
        else return x.key;
    }

    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0) return floor(x.left, key);
        Node t = floor(x.right, key);
        if (t != null) return t;
        else return x;
    }

    public Key ceiling(Key key) {
        Node x = ceiling(root, key);
        if (x == null) return null;
        else return x.key;
    }

    private Node ceiling(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0) return ceiling(x.right, key);
        Node t = ceiling(x.left, key);
        if (t != null) return t;
        else return x;
    }

    /***********************************************************************
     * Rank and selection
     ***********************************************************************/
    // select key of rank k (the key such that k others kys in the search_sort.BST are smaller)
    public Key select(int k) {
        if (k < 0 || k > size()) return null;
        Node x = select(root, k);
        return x.key;
    }

    private Node select(Node x, int k) {
        if (x == null) return null;
        int t = size(x.left);
        if (t == k) return x;
        else if (t > k) return select(x.left, k);
        else return select(x.right, k - t - 1);
    }

    // how many keys < k?
    public int rank(Key key) {
        return rank(root, key);
    }

    private int rank(Node x, Key key) {
        if (x == null) return 0;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return size(x.left);
        else if (cmp < 0) return rank(x.left, key);
        else return 1 + size(x.left) + rank(x.right, key);
    }

    /***********************************************************************
     * Range count and range search.
     ***********************************************************************/
    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        LinkedQueue<Key> queue = new LinkedQueue<Key>();
        keys(root, queue, lo, hi);
        return queue;
    }

    private void keys(Node x, LinkedQueue<Key> queue, Key lo, Key hi) {
        if (x == null) return;
        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);
        if (cmplo < 0) keys(x.left, queue, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) queue.enqueue(x.key);
        if (cmphi > 0) keys(x.right, queue, lo, hi);
    }

    public int size(Key lo, Key hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else return rank(hi) - rank(lo);
    }

    // height of search_sort.BST (one-node tree has height 0)
    public int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }

    // level order traversal
    public Iterable<Key> levelOrder() {
        LinkedQueue<Key> keys = new LinkedQueue<Key>();
        LinkedQueue<Node> queue = new LinkedQueue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.left);
            queue.enqueue(x.right);
        }
        return keys;
    }

    @Override
    // Inorder traversal
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringHelper(root, sb);
        return sb.toString();
    }

    private StringBuilder toStringHelper(Node x, StringBuilder sb) {
        if (x == null) return null;
        toStringHelper(x.left, sb);
        sb.append(x.key);
        toStringHelper(x.right, sb);
        return sb;
    }

    /*************************************************************************
     * Check integrity of search_sort.BST data structure
     *************************************************************************/
    private boolean check() {
        if (!isBST()) System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        return isBST() && isSizeConsistent() && isRankConsistent();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a search_sort.BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
    }

    // are the size fields correct?
    private boolean isSizeConsistent() {
        return isSizeConsistent(root);
    }

    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.N != size(x.left) + size(x.right) + 1) return false;
        return isSizeConsistent(x.left) && isSizeConsistent(x.right);
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    /**
     * **************************************************************************
     * Test client: can also use unit test
     * ***************************************************************************
     */
    public static void main(String[] args) {
        // read tinyST file
        String line = null;
        try {
            BufferedReader in = new BufferedReader(new FileReader("src/tinyST.txt"));
            line = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BST<String, Integer> st = new BST<String, Integer>();
        int i = 0;
        for (String str : line.split(" ")) {
            st.put(str, i);
            i++;
        }

        System.out.println("print the tree with inorder traversal: " + st);
        System.out.println("size of tree: " + st.size());
        System.out.println("height of tree: " + st.height());
        System.out.println("tree contains 'S': " + st.contains("S"));
        System.out.println("tree contains 'B': " + st.contains("B"));
        st.put("B", 100);
        System.out.println("after inserting 'B': " + st);
        st.delete("B");
        System.out.println("after deleting 'B': " + st);
        st.deleteMin();
        st.deleteMax();
        System.out.println("after deleting the min and max: " + st);
        System.out.println("the min and max are: " + st.min() + " and " + st.max());
        System.out.println("the floor and ceiling of 'B': " + st.floor("B") + " and " + st.ceiling("B"));
        System.out.println("select 1: " + st.select(1));
        System.out.println("rank of key 'C': " + st.rank("C"));

        System.out.print("all key-value pairs with level order traversal: ");
        for (String s : st.levelOrder())
            System.out.print(s + " " + st.get(s) + ", ");


        System.out.println();

        System.out.print("all key-value pairs with inorder traversal: ");
        for (String s : st.keys()) System.out.print(s + " " + st.get(s) + ", ");
        System.out.println();

        System.out.println("is the final tree a search_sort.BST: " + st.check());
    }
}