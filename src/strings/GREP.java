package strings;

import helper.StdIn;
import helper.StdOut;

/******************************************************************************
 * Compilation:  javac GREP.java
 * Execution:    java GREP regexp < input.txt
 * Dependencies: NFA.java
 * Data files:   http://algs4.cs.princeton.edu/54regexp/tinyL.txt
 * <p/>
 * This program takes an RE as a command-line argument and prints
 * the lines from standard input having some substring that
 * is in the language described by the RE.
 * <p/>
 * % more tinyL.txt
 * AC
 * AD
 * AAA
 * ABD
 * ADD
 * BCD
 * ABCCBD
 * BABAAA
 * BABBAAA
 * <p/>
 * %  java GREP "(A*B|AC)D" < tinyL.txt
 * ABD
 * ABCCBD
 ******************************************************************************/

public class GREP {
    public static void main(String[] args) {
        String re = "(.*" + args[0] + ".*)";
        NFA nfa = new NFA(re);
        while (StdIn.hasNextLine()) {
            String line = StdIn.readLine();
            if (nfa.recognizes(line)) StdOut.println(line);
        }
    }
}