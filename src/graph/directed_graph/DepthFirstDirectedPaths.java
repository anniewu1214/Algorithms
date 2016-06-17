package graph.directed_graph;

import data_structure.LinkedStack;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>DepthFirstDirectedPaths</tt> class represents a data type for finding
 * directed paths from a source vertex <em>s</em> to every
 * other vertex in the digraph.
 * <p/>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * It uses extra space (not including the graph) proportional to <em>V</em>.
 * <p/>
 */

public class DepthFirstDirectedPaths {
    private boolean[] marked;  // marked[v] = true if v is reachable from s
    private int count;  // number of vertices connected to source vertex
    private int s;  // source vertex
    private int[] edgeTo;  // edgeTo[v] = last edge on path from s to v

    // Computes a directed path from s to every other vertex in digraph G
    public DepthFirstDirectedPaths(Digraph G, int s) {
        this.s = s;
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        dfs(G, s);
    }

    private void dfs(Digraph G, int v) {
        marked[v] = true;
        count++;
        for (int w : G.adj(v)) {
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(G, w);
            }
        }
    }

    // Is there a directed path from the source vertex s to vertex v
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public int count() {
        return count;
    }

    // Returns a directed path from the source vertex s to the vertex s
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        LinkedStack<Integer> path = new LinkedStack<Integer>();
        for (int x = v; x != s; x = edgeTo[x]) {
            path.push(x);
        }
        path.push(s);
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyDG.txt")));
        Digraph G = new Digraph(in);

        int s = 0;
        DepthFirstDirectedPaths dfs = new DepthFirstDirectedPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (dfs.hasPathTo(v)) {
                System.out.printf("%d to %d:  ", s, v);
                for (int x : dfs.pathTo(v)) {
                    if (x == s) System.out.print(x);
                    else System.out.print("-" + x);
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d  : not connected\n", s, v);
            }
        }
    }
}
