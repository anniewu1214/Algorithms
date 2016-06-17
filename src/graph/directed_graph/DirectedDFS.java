package graph.directed_graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import helper.*;

/**
 * The <tt>DirectedDFS</tt> class represents a data type for
 * determining the vertices reachable from a given source vertex <em>s</em>
 * (or set of source vertices) in a digraph. For versions that find the paths,
 * see {@link DepthFirstDirectedPaths} and {@link BreadthFirstDirectedPaths}.
 * <p/>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case),
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * <p/>
 */

public class DirectedDFS {
    private boolean marked[]; // marked[v] = true if v is reachable from source( or sources)
    private int count;  // number of vertices reachable from s

    // Computes that vertices in digraph G that are reachable from the source vertex s
    public DirectedDFS(Digraph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    private void dfs(Digraph G, int s) {
        marked[s] = true;
        count++;
        for (int v : G.adj(s)) {
            if (!marked[v]) dfs(G, v);
        }
    }

    // Computes the vertices in digraph G that are connected to any of the source vertices
    public DirectedDFS(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        for (int v : sources) {
            if (!marked[v]) dfs(G, v);
        }
    }

    // Is there a directed path from the source vertex and vertex v
    public boolean marked(int v) {
        return marked[v];
    }

    // the number of vertices reachable from the source vertex
    public int count() {
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyDG.txt")));
        Digraph G = new Digraph(in);
        DirectedDFS dfs = new DirectedDFS(G, 0);
        for (int v = 0; v < G.V(); v++) {
            if (dfs.marked(v)) System.out.print(v + " ");
        }
    }

}