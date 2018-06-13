package strings;

import data_structure.Bag;
import graph.directed_graph.Digraph;
import graph.directed_graph.DirectedDFS;

import java.util.Stack;

/**
 * A simple NFA, does not support the + operator or mutiway-or, does not
 * handle character classes, meta-characters, capturing capabilities, greedy
 * vs. reluctant modifier, and other features in industrial-strength implementations
 * such as java.util.regex
 */

public class NFA {
    private char[] re;  // regular expression chars
    private Digraph G;  // digraph of epsilon transitions
    private int M;  // number of chars in regex

    // create the NFA for the given regex
    public NFA(String regex) {
        M = regex.length();
        re = regex.toCharArray();
        G = buildEpsilonTransitionDigraph();
    }

    private Digraph buildEpsilonTransitionDigraph() {
        Digraph G = new Digraph(M + 1);
        Stack<Integer> ops = new Stack<>();
        for (int i = 0; i < M; i++) {
            int lp = i;

            if (re[i] == '(' || re[i] == '|') ops.push(i);

            else if (re[i] == ')') {
                int or = ops.pop();
                if (re[or] == '|') {
                    lp = ops.pop();
                    G.addEdge(lp, or + 1);
                    G.addEdge(or, i);
                } else lp = or;
            }

            if (i < M - 1 && re[i + 1] == '*') {
                G.addEdge(lp, i + 1);
                G.addEdge(i + 1, lp);
            }

            if (re[i] == '(' || re[i] == '*' || re[i] == ')')
                G.addEdge(i, i + 1);
        }
        return G;
    }

    // Does the NFA recognize txt?
    public boolean recognizes(String txt) {

        // states reachable from start via epsilon-transition
        Bag<Integer> pc = new Bag<>();
        DirectedDFS dfs = new DirectedDFS(G, 0);
        for (int v = 0; v < G.V(); v++)
            if (dfs.marked(v)) pc.add(v);

        // compute possible NFA states for txt[i+1]
        for (int i = 0; i < txt.length(); i++) {
            Bag<Integer> match = new Bag<>();
            // match transitions
            for (int v : pc) {
                if (v == M) continue;
                if (re[v] == txt.charAt(i) || re[v] == '.') match.add(v + 1);
            }

            // set of states reachable after matching the previous char
            dfs = new DirectedDFS(G, match);
            pc = new Bag<>();
            for (int v = 0; v < G.V(); v++)
                if (dfs.marked(v)) pc.add(v);
        }

        for (int v : pc)
            if (v == M) return true;
        return false;
    }

    public static void main(String[] args) {
        NFA nfa = new NFA("(A*B|AC)D");
        System.out.println(nfa.recognizes("AAAABD"));
        System.out.println(nfa.recognizes("AAAAC"));
    }
}