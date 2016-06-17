package graph.undirected_graph;

import data_structure.LinkedQueue;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>CC</tt> class represents a data type for
 * determining the connected components in an undirected graph.
 * The <em>id</em> operation determines in which connected component
 * a given vertex lies; the <em>connected</em> operation
 * determines whether two vertices are in the same connected component;
 * the <em>count</em> operation determines the number of connected
 * components; and the <em>size</em> operation determines the number
 * of vertices in the connect component containing a given vertex.
 * <p/>
 * The <em>component identifier</em> of a connected component is one of the
 * vertices in the connected component: two vertices have the same component
 * identifier if and only if they are in the same connected component.
 * <p/>
 * <p/>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case),
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, the <em>id</em>, <em>count</em>, <em>connected</em>,
 * and <em>size</em> operations take constant time.
 * <p/>
 */
public class CC {
    private boolean marked[];  // marked[v] = has vertex v been marked?
    private int[] id; // id[v] = id of connected component containing v
    private int[] size;  // size[id] = number of vertices in given component
    private int count;  // number of connected components

    // Computes the connected components of the undirected graph
    public CC(Graph G) {
        marked = new boolean[G.V()];
        id = new int[G.V()];
        size = new int[G.V()];
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
                count++;
            }
        }
    }

    // depth-first search
    private void dfs(Graph G, int v) {
        marked[v] = true;
        id[v] = count;
        size[count]++;
        for (int w : G.adj(v)) {
            if (!marked[w]) dfs(G, w);
        }
    }

    // Returns the component id of the connected component containing vertex v
    public int id(int v) {
        return id[v];
    }

    // return the number of vertices in the connected component containing vertex v
    public int size(int v) {
        return size[id[v]];
    }

    // returns the number of connected components
    public int count() {
        return count;
    }

    // are vertices t and w in the same connected component ?
    public boolean connected(int v, int w) {
        return id(v) == id(w);
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/tinyG.txt")));
        Graph graph = new Graph(in);
        CC cc = new CC(graph);

        // number of connected components
        int M = cc.count();
        System.out.println(M + " components");

        // compute list of vertices in each connected component
        LinkedQueue<Integer>[] components = (LinkedQueue<Integer>[]) new LinkedQueue[M];
        for (int i = 0; i < M; i++) components[i] = new LinkedQueue<Integer>();
        for (int v = 0; v < graph.V(); v++) components[cc.id(v)].enqueue(v);

        // print results
        for (int i = 0; i < M; i++) {
            for (int v : components[i]) System.out.println(v + " ");
            System.out.println();
        }
    }
}
