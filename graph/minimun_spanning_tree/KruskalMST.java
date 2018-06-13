package graph.minimun_spanning_tree;

import data_structure.LinkedQueue;
import data_structure.MinPQ;
import graph.UF;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>KruskalMST</tt> class represents a data type for computing a
 * <em>minimum spanning tree</em> in an edge-weighted graph.
 * The edge weights can be positive, zero, or negative and need not
 * be distinct. If the graph is not connected, it computed a <em>minimum
 * spanning forest</em>, which is the union of minimum spanning trees
 * in each connected component. The <tt>weight()</tt> method returns the
 * weight of a MST and the <tt>edges()</tt> method returns its edges.
 * <p>
 * This implementation used <em>Kruskal's algorithm</em> and the
 * union found data type.
 * The constructor takes time proportional to <em>E</em> log <em>V</em>
 * and extra space (not including the graph) proportional to <em>V</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, th <tt>weight()</tt> method takes constant ime and the
 * <tt>edges()</tt> method takes time proportional to <em>V</em>.
 * </p>
 */
public class KruskalMST {
    private double weight;  // weight of MST
    private LinkedQueue<Edge> mst = new LinkedQueue<>();  // edges in MST

    // Compute a MST (or forest) of an edge-weighted graph.
    public KruskalMST(EdgeWeightedGraph G) {
        // more efficient to build heap by passing array of edges
        MinPQ<Edge> pq = new MinPQ<>();
        for (Edge e : G.edges()) pq.insert(e);

        // run greedy algorithm, use union found to detect cycles.
        UF uf = new UF(G.V());
        // need to test if the priority queue is empty, if the graph is not connected
        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();
            int v = e.either();
            int w = e.other(v);
            if (!uf.connected(v, w)) { // v-w does not create a cycle
                uf.union(v, w);  // merge v and w components
                mst.enqueue(e);  // add edge e to mst
                weight += e.weight();
            }
        }
    }

    // Returns the edges in a minimum spanning tree (or forest).
    public Iterable<Edge> edges() {
        return mst;
    }

    // Returns the sum of the edge weights in a minimum spanning tree (or forest).
    public double weight() {
        return weight;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyEWG.txt")));
        EdgeWeightedGraph G = new EdgeWeightedGraph(in);
        KruskalMST mst = new KruskalMST(G);
        for (Edge e : mst.edges()) {
            System.out.println(e);
        }
        System.out.printf("%.4f\n", mst.weight);
    }

}