package graph.shortest_path;

import graph.directed_graph.Topological;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * The <tt>EdgeWeightedDirectedCycle</tt> class represents a data type for
 * determining whether an edge-weighted digraph(EWD) has a directed cycle.
 * The <em>hasCycle</em> operation determines whether the EWD has a directed
 * cycle and, if so, the <em>cycle</em> operation returns one.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worse case), where <em>V</em> is the number of vertices and
 * <em>E</em> is the number of edges.Afterwards, the <em>hasCycle</em>
 * operation takes constant time; the <em>cycle</em> operation takes time
 * proportional to the length of the cycle.
 * </p>
 * <p>
 * See {@link Topological} to compute a topological order if the EWD is acyclic.
 * </p>
 */
public class EdgeWeightedDirectedCycle {
    private boolean[] marked;  // marked[v] = has vertex v been marked?
    private DirectedEdge[] edgeTo;  // edgeTo[v] = previous edge on path to v
    private boolean[] onStack;  // onStack[v] = is vertex on the stack?
    private Stack<DirectedEdge> cycle;  // directed cycle (or null if no such cycle)

    // Determines whether the edge-weighted digraph has a directed cycle, if so finds such a cycle.
    public EdgeWeightedDirectedCycle(EdgeWeightedDigraph G) {
        marked = new boolean[G.V()];
        onStack = new boolean[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        for (int v = 0; v < G.V(); v++) if (!marked[v]) dfs(G, v);
    }

    private void dfs(EdgeWeightedDigraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();

            // short circuit if directed cycle found
            if (cycle != null) return;

                // found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = e;
                dfs(G, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new Stack<>();
                while (e.from() != w) {
                    cycle.push(e);
                    e = edgeTo[e.from()];
                }
                cycle.push(e);
            }
        }
        onStack[v] = false;
    }

    // Does the edge-weighted graph have a directed cycle?
    public boolean hasCycle() {
        return cycle != null;
    }

    // returns a directed cycle if the EWD has a directed cycle, and null otherwise
    public Iterable<DirectedEdge> cycle() {
        return cycle;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyEWD.txt")));
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(in);

        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (finder.hasCycle()) {
            System.out.print("Cycle: ");
            for (DirectedEdge e : finder.cycle()) System.out.print(e + " ");
            System.out.println();
        } else System.out.println("No directed cycle");

    }
}