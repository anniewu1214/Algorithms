package strings;

import helper.BinaryStdIn;
import helper.BinaryStdOut;

/******************************************************************************
 * Compilation:  javac Genome.java
 * Execution:    java Genome - < input.txt   (compress)
 * Execution:    java Genome + < input.txt   (expand)
 * Dependencies: BinaryIn.java BinaryOut.java
 *
 * Compress or expand a genomic sequence using a 2-bit code.
 *
 * % more genomeTiny.txt
 * ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 *
 * % java Genome - < genomeTiny.txt | java Genome +
 * ATAGATGCATAGCGCATAGCTAGATGTGCTAGC
 ******************************************************************************/
public class Genome {
    private static final Alphabet DNA = new Alphabet("ACGT");

    public static void compress() {
        String s = BinaryStdIn.readString();
        int N = s.length();
        BinaryStdOut.write(N);

        for (int i = 0; i < N; i++) {
            int d = DNA.toIndex(s.charAt(i));
            BinaryStdOut.write(d, 2);
        }
        BinaryStdOut.close();
    }

    public static void expand() {
        int N = BinaryStdIn.readInt();
        for (int i = 0; i < N; i++) {
            char c = BinaryStdIn.readChar(2);
            BinaryStdOut.write(DNA.toChar(c), 8);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}