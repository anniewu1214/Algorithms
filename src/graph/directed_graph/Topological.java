package graph.directed_graph;

import graph.shortest_path.EdgeWeightedDigraph;
import graph.shortest_path.EdgeWeightedDirectedCycle;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>Topological</tt> class represents a data type for
 * determining a topological order of a directed acyclic graph (DAG).
 * Recall, a digraph has a topological order if and only if it has a DAG.
 * The <em>hasOrder</em> operation determines whether the digraph has
 * a topological order, and if so, the <em>order</em> operation returns one.
 * <p>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case), where <em>V</em> is the number of vertices and
 * <em>E</em> is the number of edges. Afterwards, the <em>hasOrder</em>
 * operation takes constant time; the <em>order</em> operation takes
 * time proportional to <em>V</em>.
 * </p>
 * <p>
 * See {@link DirectedCycle} and {@link EdgeWeightedDirectedCycle} to
 * compute a directed cycle if the digraph is not a DAG.
 * <p/>
 * </p>
 */
public class Topological {
    private Iterable<Integer> order; // topological order

    // Determines whether the digraph has a topological order and if so, finds such a order.
    public Topological(Digraph G) {
        DirectedCycle finder = new DirectedCycle(G);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        }
    }

    // Determines  whether the edge-weighted digraph has a topological order and if so, rinds such an order.
    public Topological(EdgeWeightedDigraph G) {
        EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(G);
        if (!finder.hasCycle()) {
            DepthFirstOrder dfs = new DepthFirstOrder(G);
            order = dfs.reversePost();
        }
    }

    // Returns a topological order if the digraph has one, and null otherwise
    public Iterable<Integer> order() {
        return order;
    }

    // Does the digraph have a topological order ?
    public boolean hasOrder() {
        return order != null;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyDAG.txt")));
        Digraph G = new Digraph(in);
        Topological topological = new Topological(G);
        System.out.print("The topological order of the graph:  ");
        if (topological.hasOrder()) {
            for (int v : topological.order()) {
                System.out.print(v + " ");
            }
        } else System.out.println("This graph is not DAG, and has no topological order");
    }
}