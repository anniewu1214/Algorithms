package graph.directed_graph;

import data_structure.LinkedQueue;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>KosarajuSharirSCC</tt> class represents a data type for
 * determining the strong components in a digraph.
 * The <em>id</em> operation determines in which strong component
 * a given vertex lies; the <em>areStronglyConnected</em> operation
 * determines whether two vertices are in the same strong component;
 * and the <em>count</em> operation determines the number of strong
 * components.
 * <p/>
 * The <em>component identifier</em> of a component is one of the
 * vertices in the strong component: two vertices have the same component
 * identifier if and only if they are in the same strong component.
 * <p/>
 * <p/>
 * This implementation uses the Kosaraju-Sharir algorithm.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case),
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, the <em>id</em>, <em>count</em>, and <em>areStronglyConnected</em>
 * operations take constant time.
 * For alternate implementations of the same API, see
 * {@link TarjanSCC} and {@link GabowSCC}.
 * <p/>
 */

public class KosarajuSharirSCC {
    private boolean[] marked;  // marked[v] = has vertex v been visited?
    private int[] id;  // id[v] = id of strong component containing v
    private int count;  // number of strongly-connected components

    // Compute the strong components of the digraph
    public KosarajuSharirSCC(Digraph G) {
        // compute reverse  postorder of reverse graph
        DepthFirstOrder dfs = new DepthFirstOrder(G.reverse());

        // run DFS on G, using reverse postorder to guide calculation
        marked = new boolean[G.V()];
        id = new int[G.V()];
        for (int v : dfs.reversePost()) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // run DFS on graph G
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        id[v] = count;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    // returns the number of strongly connected components
    public int count() {
        return count;
    }

    // Are vertices v, w strongly connected?
    public boolean stronglyConnected(int v, int w) {
        return id[v] == id[w];
    }

    // returns the component id of the strong component containing vertex v
    public int id(int v) {
        return id[v];
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyDG.txt")));
        Digraph G = new Digraph(in);
        KosarajuSharirSCC scc = new KosarajuSharirSCC(G);
        int M = scc.count();
        System.out.println(M + " components");

        // compute list of vertices in each strong component
        LinkedQueue<Integer>[] components = (LinkedQueue<Integer>[]) new LinkedQueue[M];
        for (int i = 0; i < M; i++) components[i] = new LinkedQueue<>();
        for (int v = 0; v < G.V(); v++) components[scc.id(v)].enqueue(v);

        // print results
        for (int i = 0; i < M; i++) {
            for (int v : components[i]) System.out.print(v + " ");
            System.out.println();
        }
    }
}