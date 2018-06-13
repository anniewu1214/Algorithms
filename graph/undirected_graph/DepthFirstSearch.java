package graph.undirected_graph;

import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Run depth first search on an undirected graph.
 * Determine the vertices connected to a given source vertex <em>s</em>.
 * Runs in O(E + V) time.
 */
public class DepthFirstSearch {
    private boolean[] marked;  // marked[v] = is there a s-v path?
    private int count;  // number of vertices connected to s

    /**
     * Compute the vertices in graph <tt>G</tt> that are connected to the source vertex <tt>s</tt>.
     *
     * @param G the graph
     * @param s the source vertex
     */
    public DepthFirstSearch(Graph G, int s) {
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    // depth first search from v
    private void dfs(Graph G, int v) {
        marked[v] = true;
        count++;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    /**
     * Is there a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>?
     *
     * @param v the vertex
     * @return <tt>true</tt> if there is a path, <tt>false</tt> otherwise
     */
    public boolean marked(int v) {
        return marked[v];
    }

    // Returns the number of vertices connected to the source vertex
    public int count() {
        return count;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyG.txt")));
        Graph graph = new Graph(in);
        DepthFirstSearch search = new DepthFirstSearch(graph, 0);
        for (int v = 0; v < graph.V(); v++) {
            if (search.marked(v)) System.out.print(v + " ");
        }
        System.out.println();

        if (search.count() != graph.V()) System.out.println("NOT connected");
        else System.out.println("connected");
    }
}
