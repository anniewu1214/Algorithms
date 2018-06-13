package graph.undirected_graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import helper.*;
/**
 * A graph, implemented using an array of sets.
 */
public class Graph {
    private In.Bag<Integer>[] adj;
    private int V;  // number of vertices
    private int E;  // number of edges

    /**
     * Initialize an empty graph with V vertices and 0 edges.
     *
     * @param V the number of vertices
     */
    public Graph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (In.Bag<Integer>[]) new In.Bag[V];
        for (int v = 0; v < V; v++) adj[v] = new In.Bag<Integer>();
    }

    /**
     * Initialize a graph from an input stream.
     * The format is the number of vertices V,
     * followed by the number of edges E,
     * followed by E paris of vertices, with each separated by whitespace.
     */
    public Graph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            addEdge(v, w);
        }

    }

    /**
     * Initialize a new graph that is a deep copy of <tt>G</tt>.
     */
    public Graph(Graph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj[v]) {
                adj[v].add(w);
            }
        }
    }

    // Returns the number of vertices in the graph.
    public int V() {
        return V;
    }

    // Returns the number of edges int the graph.
    public int E() {
        return E;
    }

    // is 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    // Adds the undirected edge v-w to the graph.
    private void addEdge(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        adj[v].add(w);
        adj[w].add(v);
        E++;
    }

    // Returns the vertices adjacent to vertex v.
    public Iterable<Integer> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    // Returns the degree of vertex v
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    // Maximum degree of graph
    public int maxDegree() {
        int max = 0;
        for (int v = 0; v < V; v++) {
            if (degree(v) > max) max = degree(v);
        }
        return max;
    }

    // average degree of graph
    public int avgDegree() {
        return 2 * E() / V();  // each edge incident on two vertices.
    }

    // number of self-loops
    public int numberOfSelfLoops() {
        int count = 0;
        for (int v = 0; v < V(); v++) {
            for (int w : adj(v)) {
                if (v == w) count++;
            }
        }
        return count / 2; // self loop appears in adjacency list twice.
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges
     * <em>E</em>, followed by the <em>V</em> adjacency lists
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String NEWLINE = System.getProperty("line.separator");
        sb.append(V).append(" vertices, ").append(E).append(" edges ").append(NEWLINE);
        for (int v = 0; v < V(); v++) {
            sb.append(v).append(": ");
            for (int w : adj[v]) sb.append(w).append(" ");
            sb.append(NEWLINE);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyG.txt")));
        Graph graph = new Graph(in);
        System.out.println(graph);

        System.out.println("vertex of maximum degree = " + graph.maxDegree());
        System.out.println("average degree           = " + graph.avgDegree());
        System.out.println("number of self loops     = " + graph.numberOfSelfLoops());
    }
}