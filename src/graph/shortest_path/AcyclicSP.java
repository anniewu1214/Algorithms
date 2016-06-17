package graph.shortest_path;

import graph.directed_graph.Topological;
import graph.shortest_path.DirectedEdge;
import graph.shortest_path.EdgeWeightedDigraph;

import java.util.Stack;

/**
 * The <tt>AcyclicSP</tt> class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted directed acyclic
 * graphs (DAGs). The edge weights can be positive, negative or zero.
 * <p>
 * This implementation uses a topological-sort based algorithm.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, the <tt>distTo()</tt> and <tt>hasPathTo()</tt> methods take
 * constant time and the <tt>pathTo()</tt> method takes time proportional to the
 * number of edges in the shortest path returned.
 * </p>
 */
public class AcyclicSP {
    private double[] distTo;
    private DirectedEdge[] edgeTo;


    /**
     * Computes a shortest paths tree from <tt>s</tt> to every other vertex in
     * the directed acyclic graph <tt>G</tt>.
     *
     * @param G the acyclic digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if the digraph is not acyclic
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public AcyclicSP(EdgeWeightedDigraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // visit vertices in topological order
        Topological topological = new Topological(G);
        if (!topological.hasOrder()) throw new IllegalArgumentException("Digraph is not acyclic.");
        for (int v : topological.order())
            for (DirectedEdge e : G.adj(v))
                relax(e);
    }

    // relax edge e
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
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
        return distTo(v) < Double.POSITIVE_INFINITY;
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
}