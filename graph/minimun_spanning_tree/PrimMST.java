
package graph.minimun_spanning_tree;

import data_structure.LinkedQueue;
import data_structure.IndexMinPQ;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>PrimMST</tt> class represents a data type for computing a
 * <em>minimum spanning tree</em> in a edge-weighted graph.
 * The edge weights can be positive, negative, or zero and need not
 * be distinct. If the graph is not connected, it computes a <em>minimum
 * spanning forest</em>, which is the union of MSTs in each connected
 * component. The <tt>weight()</tt> method returns the weight of a MST
 * and the <tt>edges()</tt> method returns its edges.
 * <p>
 * This implementation uses <em>Prim's algorithm</em> with an indexed
 * binary heap.
 * The constructor takes time proportional to <em>E</em> log <em>V</em>
 * and extra space (not including the graph) proportional to <em>V</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, the <tt>weight()</tt> method takes constant time
 * and the <tt>edges()</tt> method takes time proportional to <em>V</em>.
 * </p>
 */
public class PrimMST {
    private Edge[] edgeTo;  // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;  // distTo[v] = weight of shortest such edge
    private boolean[] marked;  // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;

    // Compute a MST (or MSF) of an edge-weighted graph.
    public PrimMST(EdgeWeightedGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<>(G.V());

        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)  // run for each vertex to find MSF
            if (!marked[v]) prim(G, v);
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(EdgeWeightedGraph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }


    // scan vertex v
    private void scan(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;
            if (e.weight() < distTo[w]) {
                distTo[w] = e.weight();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else pq.insert(w, distTo[w]);
            }
        }
    }


    // Returns the edges in a minimum spanning tree (or forest).
    public Iterable<Edge> edges() {
        LinkedQueue<Edge> mst = new LinkedQueue<>();
        for (Edge e : edgeTo) if (e != null) mst.enqueue(e);
        return mst;
    }

    // Returns the sum of the edge weights in a minimum spanning tree (or forest).
    public double weight() {
        double weight = 0.0;
        for (Edge e : edges()) weight += e.weight();
        return weight;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyEWG.txt")));
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        PrimMST mst = new PrimMST(G);
        for (Edge e : mst.edges()) System.out.println(e);
        System.out.printf("%.3f\n", mst.weight());
    }
}