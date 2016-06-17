package graph;

/**
 * Weighted quick-union by rank with path compression by halving.
 */
public class UF {
    private int[] parent; // parent[i] = parent of i
    private int[] size; // size[i] = number of objects in subtree rooted at i
    private int count; // number of components

    public UF(int N) {
        parent = new int[N];
        size = new int[N];
        count = N;
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int count() {
        return count;
    }

    private int root(int i) {
        if (i < 0 || i >= parent.length) throw new IndexOutOfBoundsException();
        while (i != parent[i]) {
            parent[i] = parent[parent[i]]; // path compression by halving
            i = parent[i];
        }
        return i;
    }

    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    public void union(int p, int q) {
        int i = root(p);
        int j = root(q);
        if (i == j) return;

        // make smaller root pointing to larger one
        if (size[i] < size[j]) {
            parent[i] = j;
            size[j] += size[i];
        } else {
            parent[j] = i;
            size[i] += size[j];
        }
        count--;
    }

    public static void main(String[] args) {
        UF uf = new UF(10);
        uf.union(4, 3);
        uf.union(3, 8);
        uf.union(6, 5);
        uf.union(9, 4);
        uf.union(2, 1);
        uf.union(5, 0);
        uf.union(7, 2);
        uf.union(6, 1);
        System.out.println(uf.count + " components");

    }
}
