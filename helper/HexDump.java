package helper;

/******************************************************************************
 * Compilation:  javac HexDump.java
 * Execution:    java HexDump < file
 * Dependencies: BinaryStdIn.java StdOut.java
 * Data file:    http://algs4.cs.princeton.edu/55compression/abra.txt
 * <p/>
 * Reads in a binary file and writes out the bytes in hex, 16 per line.
 * <p/>
 * % more abra.txt
 * ABRACADABRA!
 * <p/>
 * % java HexDump 16 < abra.txt
 * 41 42 52 41 43 41 44 41 42 52 41 21
 * 96 bits
 * <p/>
 * <p/>
 * Remark
 * --------------------------
 * - Similar to the Unix utilities od (octal dump) or hexdump (hexadecimal dump).
 * <p/>
 * % od -t x1 < abra.txt
 * 0000000 41 42 52 41 43 41 44 41 42 52 41 21
 * 0000014
 ******************************************************************************/

public class HexDump {

    public static void main(String[] args) {
        int bytesPerLine = 16;
        if (args.length == 1) {
            bytesPerLine = Integer.parseInt(args[0]);
        }

        int i;
        for (i = 0; !BinaryStdIn.isEmpty(); i++) {
            if (bytesPerLine == 0) {
                BinaryStdIn.readChar();
                continue;
            }
            if (i == 0) StdOut.printf("");
            else if (i % bytesPerLine == 0) StdOut.printf("\n", i);
            else StdOut.print(" ");
            char c = BinaryStdIn.readChar();
            StdOut.printf("%02x", c & 0xff);
        }
        if (bytesPerLine != 0) StdOut.println();
        StdOut.println((i * 8) + " bits");
    }
}
