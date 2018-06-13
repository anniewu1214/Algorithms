package graph.directed_graph;

import data_structure.LinkedStack;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>DirectedCycle</tt> class represents a data type for
 * determining whether a digraph has a directed cycle.
 * The <em>hasCycle</em> operation determines whether the digraph has
 * a directed cycle and, and of so, the <em>cycle</em> operation
 * returns one.
 * <p/>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case),
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, the <em>hasCycle</em> operation takes constant time;
 * the <em>cycle</em> operation takes time proportional
 * to the length of the cycle.
 * <p/>
 * See {@link Topological} to compute a topological order if the
 * digraph is acyclic.
 * <p/>
 */
public class DirectedCycle {
    private boolean[] marked;  // marked[v] = has vertex v been marked?
    private int[] edgeTo;  // edgeTo[v] = previous vertex on path to v
    private LinkedStack<Integer> cycle;  // directed cycle (or null if no such cycle)
    private boolean[] onStack;  // onStack[v] = is vertex on the stack

    // Determines whether the digraph has a directed cycle and if so finds such a cycle.
    public DirectedCycle(Digraph G) {
        onStack = new boolean[G.V()];
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) if (!marked[v]) dfs(G, v);
    }

    // check that algorithm computes either the topological order or finds a directed cycle
    private void dfs(Digraph G, int v) {
        onStack[v] = true;
        marked[v] = true;
        for (int w : G.adj(v)) {
            // short circuit if directed cycle found
            if (cycle != null) return;

                // found new vertex, so recur
            else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }

            // trace back directed cycle
            else if (onStack[w]) {
                cycle = new LinkedStack<>();
                for (int x = v; x != w; x = edgeTo[x]) cycle.push(x);
                cycle.push(w);
                cycle.push(v);
            }
        }
        onStack[v] = false;
    }

    // Does the digraph have a directed cycle?
    public boolean hasCycle() {
        return cycle != null;
    }

    // Returns a directed cycle if the digraph has a directed cycle, and null otherwise.
    public Iterable<Integer> cycle() {
        return cycle;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyDAG.txt")));
        Digraph G = new Digraph(in);
        DirectedCycle cycle = new DirectedCycle(G);
        if (cycle.hasCycle()) {
            for (int v : cycle.cycle()) System.out.print(v + "  ");
        } else System.out.println("Graph is acyclic");
    }

}