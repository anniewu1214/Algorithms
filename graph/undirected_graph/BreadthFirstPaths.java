package graph.undirected_graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import data_structure.*;
import helper.*;

/**
 * The <tt>BreadthFirstPaths</tt> class represents a data type for finding
 * shortest paths (number of edges) from a source vertex <em>s</em>
 * (or a set of source vertices)
 * to every other vertex in an undirected graph.
 * <p/>
 * This implementation uses breadth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * It uses extra space (not including the graph) proportional to <em>V</em>.
 * <p/>
 */
public class BreadthFirstPaths {
    public static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;  // marked[v] = is there an s-v path
    private int[] edgeTo;  // edgeTo[v] = previous edge on shortest s-v path
    private int[] distTo;  // disTo[v] = number of edges shortest s-v path

    // Computes the shortest path between the source vertex s and every other vertex in the graph
    public BreadthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        bfs(G, s);
    }

    // breadth-first search from a single source
    private void bfs(Graph G, int s) {
        LinkedQueue<Integer> q = new LinkedQueue<Integer>();
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        distTo[s] = 0;
        marked[s] = true;
        q.enqueue(s);

        while (!q.isEmpty()) {
            int v = q.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    q.enqueue(w);
                }
            }
        }
    }

    // Is there a path between the source vertex s and vertex v
    public boolean hasPathTo(int v) {
        return marked[v];
    }

    // Returns the number of edges in a shortest path between the source vertex s and vertex v
    public int distTo(int v) {
        return distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        LinkedStack<Integer> path = new LinkedStack<Integer>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x]) path.push(x);
        path.push(x);
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyG.txt")));
        Graph graph = new Graph(in);

        int s = 0;
        BreadthFirstPaths bfs = new BreadthFirstPaths(graph, s);

        for (int v = 0; v < graph.V(); v++) {
            if (bfs.hasPathTo(v)) {
                System.out.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) System.out.print(x);
                    else System.out.print("-" + x);
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d (-):  not connected\n", s, v);
            }
        }
    }
}