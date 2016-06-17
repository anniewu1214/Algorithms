package graph.shortest_path;

import helper.In;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The <tt>CPM</tt> class provides a client that solves the
 * parallel precedence-constrained job scheduling problem
 * via the <em>critical path method</em>. It reduces the problem
 * to the longest-path problem in edge weighted DAGs.
 * It builds an edge-weighted digraph (which must be a DAG)
 * from the job-scheduling problem specification,
 * finds the longest-paths tree, and computes the longest-paths
 * lengths (which are precisely the start time for each job).
 * <p>
 * This implementation uses {@link AcyclicLP} to find a longest
 * path in a DAG.
 * The running time is proportional to <em>V</em> + <em>E</em>,
 * where <em>V</em> is the number of jobs and <em>E</em> is the
 * number of precedence constraints.
 * </p>
 */
public class CPM {
    // this class cannot be instantiated
    private CPM() {
    }

    /**
     * Reads the precedence constraints from standard input
     * and prints a feasible schedule to standard output.
     */
    public static void main(String[] args) throws FileNotFoundException {
        In in = new In(new Scanner(new File("src/graph/jobsPC.txt")));

        // number of jobs
        int N = in.readInt();

        // source and sink
        int source = 2 * N;
        int sink = 2 * N + 1;

        // build network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2 * N + 2);
        for (int i = 0; i < N; i++) {
            double duration = in.readDouble();
            G.addEdge(new DirectedEdge(source, i, 0));
            G.addEdge(new DirectedEdge(i, i + N, duration));
            G.addEdge(new DirectedEdge(i + N, sink, 0));

            // precedence constraints
            int M = in.readInt();
            for (int j = 0; j < M; j++) {
                int precedent = in.readInt();
                G.addEdge(new DirectedEdge(i + N, precedent, 0));
            }
        }

        // compute longest path
        AcyclicLP lp = new AcyclicLP(G, source);

        // print results
        System.out.println(" job   start  finish");
        System.out.println("--------------------");
        for (int i = 0; i < N; i++) System.out.printf("%4d %7.1f %7.1f\n", i, lp.distTo(i), lp.distTo(i + N));
        System.out.printf("Finish time: %7.1f\n", lp.distTo(sink));
    }
}