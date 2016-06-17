package graph.undirected_graph;

import data_structure.LinkedStack;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>Cycle</tt> class represents a data type for
 * determining whether an undirected graph has a cycle.
 * The <em>hasCycle</em> operation determines whether the graph has
 * a cycle and, if so, the <em>cycle</em> operation returns one.
 * <p/>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case),
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, the <em>hasCycle</em> operation takes constant time;
 * the <em>cycle</em> operation takes time proportional
 * to the length of the cycle.
 * <p/>
 */
public class Cycle {
    private boolean[] marked;
    private int[] edgeTo;
    private LinkedStack<Integer> cycle;

    public Cycle(Graph G) {
        if (hasSelfLoop(G)) return;
        if (hasParallelEdges(G)) return;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        for (int s = 0; s < G.V(); s++) {
            if (!marked[s]) dfs(G, s, s);
        }
    }

    private void dfs(Graph G, int v, int u) {
        marked[v] = true;
        for (int w : G.adj(v)) {

            // short circuit if cycle already found
            if (cycle != null) return;

            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w, v);
            }
            // check for cycle (but disregard reverse of edge leading to v)
            else if (w != u) {
                cycle = new LinkedStack<>();
                for (int x = v; x != w; x = edgeTo[x]) cycle.push(x);
                cycle.push(w);
                cycle.push(v);
            }
        }
    }

    // does this graph have two parallel edges?
    private boolean hasParallelEdges(Graph G) {
        for (int v = 0; v < G.V(); v++) {
            marked = new boolean[G.V()];
            for (int w : G.adj(v)) {
                if (marked[w]) {
                    cycle = new LinkedStack<>();
                    cycle.push(v);
                    cycle.push(w);
                    cycle.push(v);
                    return true;
                }
                marked[w] = true;
            }
        }
        return false;
    }

    // does this graph have a self loop?
    private boolean hasSelfLoop(Graph G) {
        for (int v = 0; v < G.V(); v++) {
            for (int w : G.adj(v)) {
                if (v == w) {
                    cycle = new LinkedStack<>();
                    cycle.push(v);
                    cycle.push(v);
                    return true;
                }
            }
        }
        return false;
    }


    public boolean hasCycle() {
        return cycle != null;
    }

    public Iterable<Integer> cycle() {
        return cycle;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyG.txt")));
        Graph G = new Graph(in);
        Cycle cycle = new Cycle(G);
        if (cycle.hasCycle()) {
            for (int v : cycle.cycle()) System.out.print(v + "  ");
        } else System.out.println("Graph is acyclic");
    }
}

