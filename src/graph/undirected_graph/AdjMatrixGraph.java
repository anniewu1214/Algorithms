package graph.undirected_graph;

import helper.In;

/**
 * A graph implemented using an adjacency matrix.
 */
public class AdjMatrixGraph {
    private int V;  // number of vertices
    private int E;  // number of edges
    private boolean[][] adj;  // adjacency matrix

    // empty graph with V vertices
    public AdjMatrixGraph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = new boolean[V][V];
    }

    // random graph with V vertices and E edges
    public AdjMatrixGraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        if (E > V * V) throw new RuntimeException("Too many edges");
        // can be inefficient
        while (this.E != E) {
            int v = (int) (V * Math.random());
            int w = (int) (V * Math.random());
            addEdge(v, w);
        }
    }

    // number of vertices and edges
    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    // add undirected edge v-w
    private void addEdge(int v, int w) {
        if (!adj[v][w]) E++;
        adj[v][w] = true;
        adj[w][v] = true;
    }

    // does the graph contain the edge v-w?
    public boolean contains(int v, int w) {
        return adj[v][w];
    }

    // return the list of neighbors of v
    public Iterable<Integer> adj(int v) {
        In.Bag<Integer> bag = new In.Bag<Integer>();
        for (int i = 0; i < adj[v].length; i++) if (adj[v][i]) bag.add(i);
        return bag;
    }

    @Override
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V).append(" ").append(E).append(NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (int w : adj(v)) s.append(w).append(" ");
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) {
        AdjMatrixGraph graph = new AdjMatrixGraph(5, 10);
        System.out.println(graph);
    }
}