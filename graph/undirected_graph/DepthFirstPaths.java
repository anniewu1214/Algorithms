package graph.undirected_graph;

import data_structure.LinkedStack;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>DepthFirstPaths</tt> class represents a data type for finding
 * paths from a source vertex <em>s</em> to every other vertex
 * in an undirected graph.
 * <p/>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * It uses extra space (not including the graph) proportional to <em>V</em>.
 * <p/>
 */
public class DepthFirstPaths {
    private boolean[] marked;  // marked[v] = is there an s-v path?
    private int[] edgeTo;  // edgeTo[v] = last edge on s-v path
    private int s;  // source vertex

    // Find path in G from s
    public DepthFirstPaths(Graph G, int s) {
        this.s = s;
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];
        dfs(G, s);
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    // Is there a path from s to v?
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // path from s to v, null if no path
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        LinkedStack<Integer> path = new LinkedStack<Integer>();
        for (int x = v; x != s; x = edgeTo[x]) path.push(x);
        path.push(s);
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyG.txt")));
        Graph graph = new Graph(in);
        int s = 0;
        DepthFirstPaths dfs = new DepthFirstPaths(graph, s);
        for (int v = 0; v < graph.V(); v++) {
            if (dfs.hasPathTo(v)) {
                System.out.printf("%d to %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) System.out.print(x);
                    else System.out.print("-" + x);
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d:  not connected\n", s, v);
            }
        }
    }
}
