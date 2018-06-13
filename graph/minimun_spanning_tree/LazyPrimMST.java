package graph.minimun_spanning_tree;

import data_structure.LinkedQueue;
import data_structure.MinPQ;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>LazyPrimMST</tt> class represents a data type for computing a
 * <em>minimum spanning tree</em> in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not be
 * distinct. If the graph is not connected, it computes a <em>minimum
 * spanning forest</em>, which is the union of minimum spanning trees in
 * each of a minimum spanning tree and the <tt>edges()</tt> method
 * returns its edges.
 * <p>
 * This implementation uses a lazy versiont of <em>Prim's algorithm</em>
 * with a binary heap of edges.
 * The constructor takes time proportional to <em>E</em> log <em>E</em>
 * and extra space (not including the graph) proportional to <em>E</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number
 * of edges.
 * Afterwords, the <tt>weight()</tt> method takes constant time and the
 * <tt>edges()</tt> method takes time proportional to <em>V</em>.
 * </p>
 */
public class LazyPrimMST {
    private double weight;  // total weight of MST
    private boolean marked[];   // marked[v] = true if v on the tree
    private LinkedQueue<Edge> mst;  // edges in the MST
    private MinPQ<Edge> pq;  // edges with one endpoint in tree

    // Computes a MST (or forest) of the edge weighted graph.
    public LazyPrimMST(EdgeWeightedGraph G) {
        marked = new boolean[G.V()];
        mst = new LinkedQueue<>();
        pq = new MinPQ<>();

        for (int v = 0; v < G.V(); v++) // run Prim from all vertices to get a minimum spanning forest
            if (!marked[v]) prim(G, v);
    }

    // run Prim's algorithm
    private void prim(EdgeWeightedGraph G, int s) {
        visit(G, s);
        while (!pq.isEmpty() && mst.size() < G.V() - 1) { // better to stop when mst has V-1 edges
            Edge e = pq.delMin();  // smallest edge on pq
            int v = e.either(), w = e.other(v);  // two endpoints
            if (marked[v] && marked[w]) continue;  // lazy, both v and w already scanned
            mst.enqueue(e);
            weight += e.weight();
            if (!marked[v]) visit(G, v);  // v becomes part of tree
            if (!marked[w]) visit(G, w);  // w becomes part of tree
        }
    }


    // add all edges e incident to v onto pq if the other endpoint has not yet been visited
    private void visit(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            if (!marked[e.other(v)]) {
                pq.insert(e);
            }
        }
    }

    // returns the edges in a MST (or forest)
    public Iterable<Edge> edges() {
        return mst;
    }

    // returns the sum of the dege weights in a MST (or forest)
    public double weight() {
        return weight;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyEWG.txt")));
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        LazyPrimMST mst = new LazyPrimMST(G);
        for (Edge e : mst.edges()) System.out.println(e);
        System.out.printf("%.3f\n", mst.weight());
    }
}