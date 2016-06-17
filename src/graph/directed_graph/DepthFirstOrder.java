package graph.directed_graph;

import data_structure.LinkedQueue;
import data_structure.LinkedStack;
import graph.shortest_path.DirectedEdge;
import graph.shortest_path.EdgeWeightedDigraph;
import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>DepthFirstOrder</tt> class represents a data type for
 * determining depth-first search ordering of the vertices in a digraph
 * or edge-weighted digraph, including preorder, postorder, and reverse postorder.
 * <p/>
 * This implementation uses depth-first search.
 * The constructor takes time proportional to <em>V</em> + <em>E</em>
 * (in the worst case),
 * where <em>V</em> is the number of vertices and <em>E</em> is the number of edges.
 * Afterwards, the <em>preorder</em>, <em>postorder</em>, and <em>reverse postorder</em>
 * operation takes take time proportional to <em>V</em>.
 * <p/>
 * <p/>
 */
public class DepthFirstOrder {
    private boolean[] marked;  // marked[v] = has v been marked in dfs?
    private int[] pre;  // pre[v] = preOrder number of v
    private int[] post;  // post[v] = postOrder number of v
    private LinkedQueue<Integer> preOrder;  // vertices in pre order
    private LinkedQueue<Integer> postOrder; // vertices in post order
    private int preCounter;  // counter or pre order numbering
    private int postCounter;  // counter or pre order numbering

    // Determines a depth-first order for the digraph G
    public DepthFirstOrder(Digraph G) {
        pre = new int[G.V()];
        post = new int[G.V()];
        postOrder = new LinkedQueue<>();
        preOrder = new LinkedQueue<>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) if (!marked[v]) dfs(G, v);
    }

    // Determines a depth-first order for the edge-weighted digraph
    public DepthFirstOrder(EdgeWeightedDigraph G) {
        pre = new int[G.V()];
        post = new int[G.V()];
        preOrder = new LinkedQueue<>();
        postOrder = new LinkedQueue<>();
        marked = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++) if (!marked[v]) dfs(G, v);
    }

    // run DFS in digraph G from vertex v and compute preOrder/postOrder
    private void dfs(Digraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preOrder.enqueue(v);

        for (int w : G.adj(v)) if (!marked[w]) dfs(G, w);

        postOrder.enqueue(v);
        post[v] = postCounter++;
    }

    // run DFS in edge-weighted digraph G from vertex v and compute preOrder/postOrder
    private void dfs(EdgeWeightedDigraph G, int v) {
        marked[v] = true;
        pre[v] = preCounter++;
        preOrder.enqueue(v);
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            if (!marked[w]) dfs(G, w);
        }
        postOrder.enqueue(v);
        post[v] = postCounter++;
    }

    // returns the pre order number of vertex v
    public int pre(int v) {
        return pre[v];
    }

    // returns the post order number of vertex v
    public int post(int v) {
        return post[v];
    }

    // returns the vertices in pre order
    public Iterable<Integer> pre() {
        return preOrder;
    }

    // returns the vertices in post order
    public Iterable<Integer> post() {
        return postOrder;
    }

    // returns the vertices in reverse post order
    public Iterable<Integer> reversePost() {
        LinkedStack<Integer> reverse = new LinkedStack<>();
        for (int v : postOrder) reverse.push(v);
        return reverse;
    }

    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/tinyDAG.txt")));
        Digraph G = new Digraph(in);
        DepthFirstOrder dfs = new DepthFirstOrder(G);

        System.out.print("Preorder:           ");
        for (int v : dfs.pre()) System.out.print(v + " ");
        System.out.println();

        System.out.print("Postorder:          ");
        for (int v : dfs.post()) System.out.print(v + " ");
        System.out.println();

        System.out.print("Reverse postorder:  ");
        for (int v : dfs.reversePost()) System.out.print(v + " ");
        System.out.println();
    }
}