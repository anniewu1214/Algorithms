package graph.directed_graph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import data_structure.*;
import helper.*;

/**
 * The <tt>BreadthDirectedFirstPaths</tt> class represents a data type for finding
 * shortest paths (number of edges) from a source vertex <em>s</em>
 * (or set of source vertices) to every other vertex in the digraph.
 * <p/>
 * This implementation uses breadth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>,
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * It uses extra space (not including the digraph) proportional to <em>V</em>.
 * <p/>
 */
public class BreadthFirstDirectedPaths {
    private static final int INFINITY = Integer.MAX_VALUE;
    private boolean[] marked;
    private int[] edgeTo;
    private int[] distTo;

    public BreadthFirstDirectedPaths(Digraph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        bfs(G, s);
    }

    public BreadthFirstDirectedPaths(Digraph G, Iterable<Integer> sources) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        for (int v = 0; v < G.V(); v++) distTo[v] = INFINITY;
        bfs(G, sources);
    }

    private void bfs(Digraph G, Iterable<Integer> sources) {
        LinkedQueue<Integer> q = new LinkedQueue<>();

        for (int s : sources) {
            marked[s] = true;
            distTo[s] = 0;
            q.enqueue(s);
        }

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

    private void bfs(Digraph G, int s) {
        LinkedQueue<Integer> queue = new LinkedQueue<Integer>();
        marked[s] = true;
        distTo[s] = 0;
        queue.enqueue(s);

        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            for (int w : G.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    marked[w] = true;
                    queue.enqueue(w);
                }
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public int distTo(int v) {
        return distTo[v];
    }

    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        LinkedStack<Integer> path = new LinkedStack<>();
        int x;
        for (x = v; distTo[x] != 0; x = edgeTo[x]) path.push(x);
        path.push(x);
        return path;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyDG.txt")));
        Digraph G = new Digraph(in);

        int s = 3;
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(G, s);

        for (int v = 0; v < G.V(); v++) {
            if (bfs.hasPathTo(v)) {
                System.out.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) System.out.print(x);
                    else System.out.print("->" + x);
                }
                System.out.println();
            } else {
                System.out.printf("%d to %d (-):  not connected \n", s, v);
            }
        }
    }
}