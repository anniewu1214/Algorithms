package graph.minimun_spanning_tree;

import data_structure.Bag;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>EdgeWeightedGraph</tt> class represents an edg-weighted
 * graph of vertices named 0 through <em>V - 1</em>, where each
 * undirected edge is of type {@link Edge} and has a real-valued weight.
 * It supports the following two primary operations: add and edge to the graph,
 * iterate over all of the edges incident to a vertex. It also provides
 * methods for returning the number of vertices <em>V</em> and the number
 * of edges <em>E</em>. Parallel edges and self-loops are permitted.
 * <p>
 * This implementation uses an adjacency-lists representation, which
 * is a vertex-indexed array of {@link Bag} objects.
 * All operations take constant time (in the worst case) except
 * iterating over the edges incident to a given vertex, which takes
 * time proportional to the number of such edges.
 * </p>
 */
public class EdgeWeightedGraph {
    private int V;
    private int E;
    private Bag<Edge>[] adj;

    // Initializes an empty edge-weighted graph with V vertices and 0 edges.
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        adj = (Bag<Edge>[]) new Bag[V];
        for (int v = 0; v < V; v++) adj[v] = new Bag<>();
    }

    // Initializes a random edge-weighted graph with V vertices and E edges.
    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) throw new IllegalArgumentException("Number of edges must be non-negative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            double weight = Math.round(100 * Math.random()) / 100.0;
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

    // Initializes an edge-weighted graph from an input stream.
    public EdgeWeightedGraph(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) throw new IllegalArgumentException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = in.readInt();
            int w = in.readInt();
            double weight = in.readDouble();
            Edge e = new Edge(v, w, weight);
            addEdge(e);
        }
    }

    // Initializes a new edge-weighted graph that is a deep copy of G
    public EdgeWeightedGraph(EdgeWeightedGraph G) {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); v++) for (Edge e : G.adj(v)) adj[v].add(e);
    }

    // throw an IndexOutOfBoundsException unless 0 <= v < V
    private void validateVertex(int v) {
        if (v < 0 || v >= V) throw new IndexOutOfBoundsException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    // Adds the undirected edge e to the edge-weighted graph.
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        adj[w].add(e);
        E++;
    }

    // Returns the number of vertices in the edge-weighted graph.
    public int V() {
        return V;
    }

    // Returns the number of edges in the edge-weighted graph.
    public int E() {
        return E;
    }

    // Returns the edges incident on vertex v.
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    // Returns the degree of vertex v.
    public int degree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    // Returns all edges in the edge-weighted graph.
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<>();
        for (int v = 0; v < V; v++) {
            int selfLoop = 0;
            for (Edge e : adj(v)) {
                if (e.other(v) > v) list.add(e);
                    // only add one copy of each self loop (self loops will be consecutive)
                else if (e.other(v) == v) {
                    if (selfLoop % 2 == 0) list.add(e);
                    selfLoop++;
                }
            }
        }
        return list;
    }

    @Override
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V).append(" ").append(E).append(NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v).append(": ");
            for (Edge e : adj[v]) s.append(e).append(" ");
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyEWG.txt")));
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        System.out.println(G);
    }
}