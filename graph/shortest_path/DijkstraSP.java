package graph.shortest_path;

import data_structure.IndexMinPQ;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * The <tt>DijkstraSP</tt> class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted digraphs where
 * the edge weights are non-negative.
 * <p>
 * The implementation uses Dijkstra's algorithm with a binary heap.
 * The constructor take time proportional to <em>E</em> log <em>V</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * After words, the <tt>distTo()</tt> and <tt>hasPathTo()</tt> methods take constant time
 * and the <tt>pathTo()</tt> method takes time proportional to the number of edges in
 * the shortest path returned.
 * <p/>
 * </p>
 */
public class DijkstraSP {
    private double[] distTo;  // distTo[v] = distance of shortest s->v path
    private DirectedEdge[] edgeTo;  // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;  // priority queue of vertices

    /**
     * Computes a shortest paths tree from <tt>s</tt> to every other vertex in
     * the edge-weighted digraph <tt>G</tt>.
     *
     * @param G the edge-weighted digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        for (DirectedEdge e : G.edges())
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");

        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v))
                relax(e);
        }
    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else pq.insert(w, distTo[w]);
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     *
     * @param v the destination vertex
     * @return the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>;
     * <tt>Double.POSITIVE_INFINITY</tt> if no such path
     */
    public double distTo(int v) {
        return distTo[v];
    }

    /**
     * Is there a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>?
     *
     * @param v the destination vertex
     * @return <tt>true</tt> if there is a path from the source vertex
     * <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     *
     * @param v the destination vertex
     * @return a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>
     * as an iterable of edges, and <tt>null</tt> if no such path
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) path.push(e);
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyEWD.txt")));
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

        int s = 0;
        DijkstraSP sp = new DijkstraSP(G, s);  // compute shortest paths

        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                System.out.printf("%d to %d (%.2f)   ", s, t, sp.distTo(t));
                for (DirectedEdge e : sp.pathTo(t)) System.out.print(e + "   ");
                System.out.println();
            } else System.out.printf("%d to %d         no path\n", s, t);
        }
    }
}