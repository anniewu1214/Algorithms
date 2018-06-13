package graph.shortest_path;

import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>Arbitrage</tt> class provides a client that finds an arbitrage
 * opportunity in a currency exchange table by constructing a
 * complete-digraph representation of the exchange table and then finding
 * a negative cycle in the digraphs.
 * <p>
 * This implementation uses the Bellman-Ford algorithm to find a
 * negative cycle in the complete digraph.
 * The running time is proportional to <em>V</em><sup>3</sup> in the
 * worst case, where <em>V</em> is the number of currencies.
 * </p>
 */
public class Arbitrage {

    // this class cannot be instantiated
    private Arbitrage() {
    }

    /**
     * Reads the currency exchange table from standard input and
     * prints an arbitrage opportunity to standard output (if one exists).
     */
    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/rates.txt")));

        // V currencies
        int V = in.readInt();
        String[] name = new String[V];

        // create complete network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);
        for (int v = 0; v < V; v++) {
            name[v] = in.readString();
            for (int w = 0; w < V; w++) {
                double rate = in.readDouble();
                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate));
                G.addEdge(e);
            }
        }

        // find negative cycle
        BellmanFordSP spt = new BellmanFordSP(G, 0);
        if (spt.hasNegativeCycle()) {
            double stake = 1000.0;
            for (DirectedEdge e : spt.negativeCycle()) {
                System.out.printf("%10.5f %s", stake, name[e.from()]);
                stake *= Math.exp(-e.weight());
                System.out.printf("= %10.5f %s\n", stake, name[e.to()]);
            }
        } else {
            System.out.println("No arbitrage opportunity");
        }
    }
}
