package graph.shortest_path;

import data_structure.LinkedQueue;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * The <tt>BellmanFordSP</tt> class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted digraph with
 * no negative cycles.
 * The edge weights can be positive, negative, or zero.
 * This class finds either a shortest path from the source vertex <em>s</em>
 * to every other vertex or a negative cycle reachable from the source vertext.
 * <p>
 * This implementation uses the Bellman-Ford-Moore algorithm.
 * The constructor takes time proportional to <em>V</em> (<em>V</em> + <em>E</em>)
 * in the worst case, where <em>V</em> is the number of vertices and <em>E</em>
 * is the number of edges.
 * Afterwards, the <tt>distTo()</tt>, <tt>hasPathTo()</tt>, and <tt>hasNegativeCycle()</tt>
 * methods take constant time; the <tt>pathTo()</tt> and <tt>negativeCycle()</tt>
 * method takes time proportional to the number of edges returned.
 * </p>
 */
public class BellmanFordSP {
    private double[] distTo; // distTo[v] = distance of shortest s->v path
    private DirectedEdge[] edgeTo;  //  edgeTo[v] = last edge on shortest s->v path
    private boolean[] onQueue;  //  onQueue[v] = is v currently on the queue
    private LinkedQueue<Integer> queue;  // queue of vertices to relax
    private int cost;  //  number of calls to relax()
    private Iterable<DirectedEdge> cycle;  // negative cycle (or null if no such cycle)

    /**
     * Computes a shortest paths tree from <tt>s</tt> to every other vertex in the
     * edge-weighted digraph <tt>G</tt>
     *
     * @param G the acyclic digraph
     * @param s the source vertex
     */
    public BellmanFordSP(EdgeWeightedDigraph G, int s) {
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        onQueue = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // Bellman-Ford algorithm
        queue = new LinkedQueue<>();
        queue.enqueue(s);
        onQueue[s] = true;

        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.dequeue();
            onQueue[v] = false;
            relax(G, v);
        }
    }

    // relax vertex v and put other endpoints on queue if changed
    private void relax(EdgeWeightedDigraph G, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (distTo[w] > distTo[v] + e.weight()) {
                distTo[w] = distTo[v] + e.weight();
                edgeTo[w] = e;
                if (!onQueue[w]) {
                    queue.enqueue(w);
                    onQueue[w] = true;
                }
            }
            if (cost++ % G.V() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) return;
            }
        }
    }

    /**
     * Is there a negative cycle reachable from the source vertex <tt>s</tt>?
     *
     * @return <tt>true</tt> if there is negative cycle reachable from the source
     * vertex <tt>s</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    // by finding a cycle in predecessor graph
    public void findNegativeCycle() {
        int V = edgeTo.length;
        EdgeWeightedDigraph spt = new EdgeWeightedDigraph(V);
        for (DirectedEdge e : edgeTo) if (e != null) spt.addEdge(e);
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
        cycle = finder.cycle();
    }

    /**
     * Returns a negative cycle reachable from the source vertex <tt>s</tt>, or <tt>null</tt>
     * if there is no such cycle.
     *
     * @return a negative cycle reachable from the soruce vertex <tt>s</tt>
     * as an iterable of edges, and <tt>null</tt> if there is no such cycle
     */
    public Iterable<DirectedEdge> negativeCycle() {
        return cycle;
    }

    /**
     * Returns the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     *
     * @param v the destination vertex
     * @return the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>;
     * <tt>Double.POSITIVE_INFINITY</tt> if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *                                       from the source vertex <tt>s</tt>
     */
    public double distTo(int v) {
        if (hasNegativeCycle()) throw new UnsupportedOperationException("Negative cost cycle exists");
        return distTo[v];
    }

    /**
     * Is there a path from the source <tt>s</tt> to vertex <tt>v</tt>?
     *
     * @param v the destination vertex
     * @return <tt>true</tt> if there is a path from the source vertex
     * <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return distTo(v) < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source <tt>s</tt> to vertex <tt>v</tt>.
     *
     * @param v the destination vertex
     * @return a shortest path from the source <tt>s</tt> to vertex <tt>v</tt>
     * as an iterable of edges, and <tt>null</tt> if no such path
     * @throws UnsupportedOperationException if there is a negative cost cycle reachable
     *                                       from the source vertex <tt>s</tt>
     */
    public Iterable<DirectedEdge> pathTo(int v) {
        if (hasNegativeCycle()) throw new UnsupportedOperationException("Negative cost cycle exists");
        if (!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<>();
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) path.push(e);
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyEWD.txt")));
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);
        int s = 0;
        BellmanFordSP bf = new BellmanFordSP(G, 0);

        if (bf.hasNegativeCycle()) {
            System.out.println("Negative cycle found: ");
            for (DirectedEdge e : bf.negativeCycle()) System.out.print(e + "  ");
        } else {
            for (int v = 0; v < G.V(); v++) {
                if (bf.hasPathTo(v)) {
                    System.out.printf("%d to %d (%5.2f)  ", s, v, bf.distTo(v));
                    for (DirectedEdge e : bf.pathTo(v)) System.out.print(e + "  ");
                    System.out.println();
                } else {
                    System.out.printf("%d to %d           no path\n", s, v);
                }
            }
        }
    }
}