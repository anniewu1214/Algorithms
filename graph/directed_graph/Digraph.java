package graph.directed_graph;

import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>Digraph</tt> class represents a directed graph of vertices
 * named 0 through <em>V</em> - 1.
 * It supports the following two primary operations: add an edge to the digraph,
 * iterate over all of the vertices adjacent from a given vertex.
 * Parallel edges and self-loops are permitted.
 * <p/>
 * This implementation uses an adjacency-lists representation, which
 * is a vertex-indexed array of {@link In.Bag} objects.
 * All operations take constant time (in the worst case) except
 * iterating over the vertices adjacent from a given vertex, which takes
 * time proportional to the number of such vertices.
 * <p/>
 */
public class Digraph {
    private In.Bag<Integer>[] adj;
    private int V;
    private int E;

    // Initialize an empty digraph with V vertices
    public Digraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (In.Bag<Integer>[]) new In.Bag[V];
        for (int i = 0; i < V; i++) adj[i] = new In.Bag<Integer>();
    }

    // Initialize a digraph from an input stream
    public Digraph(In in) {
        this.V = in.readInt();
        if (V < 0)
            throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
        adj = (In.Bag<Integer>[]) new In.Bag[V];
        for (int v = 0; v < V; v++) adj[v] = new In.Bag<Integer>();


        int E = in.readInt();
        if (E < 0)
            throw new IllegalArgumentException("Number of edges in a Digraph must be nonnegative");

        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w);
        }
    }

    // Initialize a new digraph that is a deep copy of G
    public Digraph(Digraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            // reverse so that adjacency list is in same order as original
            for (int w : G.adj[v]) adj[v].add(w);
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    public void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        E++;
    }

    public int outDegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    public Digraph reverse() {
        Digraph R = new Digraph(V);
        for (int v = 0; v < V; v++) {
            for (int w : adj(v)) R.addEdge(w, v);
        }
        return R;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public Iterable<Integer> adj(int v) {
        return adj[v];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        sb.append(V).append(" vertices, ").append(E).append(" edges ").append(NEWLINE);
        for (int v = 0; v < V; v++) {
            sb.append(String.format("%d: ", v));
            for (int w : adj(v)) {
                sb.append(String.format("%d ", w));
            }
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyDG.txt")));
        Digraph graph = new Digraph(in);
        System.out.println(graph);
    }
}
