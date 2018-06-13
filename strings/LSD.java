package strings;

import java.util.Arrays;

/**
 * LSD radix sort
 * - Sort a String[] array of N extended ASCII strings (R = 256), each of length W
 * - Sort an int[] array of N 32-bit integers, treating each integer as a
 * sequence of W = 4 bytes (R = 256)
 */
public class LSD {

    public static final int BITS_PER_BYTE = 8;

    // LSD radix sort
    public static void sort(String[] a, int W) {
        // check that strings have fixed length
        for (String str : a)
            if (str.length() != W)
                throw new IllegalArgumentException("String length error.");

        int N = a.length;
        int R = 256;  // extended ASCII alphabet size
        String[] aux = new String[N];

        // sort by key-indexed counting on dth character
        for (int d = W - 1; d >= 0; d--) {
            int[] count = new int[R + 1];
            for (String str : a) count[str.charAt(d) + 1]++; // compute frequency counts
            for (int r = 0; r < R; r++) count[r + 1] += count[r]; // compute cumulates
            for (String str : a) aux[count[str.charAt(d)]++] = str; // move data
            //System.out.println(Arrays.toString(count));
            System.arraycopy(aux, 0, a, 0, N); // copy back
        }
    }


    // LSD sort an array of integers, treating each int as 4 bytes
    // [ 2-3x faster than Arrays.sort() ]
    public static void sort(int[] a) {
        int W = 32 / BITS_PER_BYTE;  // each int is 4 bytes
        int R = 1 << BITS_PER_BYTE;  // each byte is between 0 and 255
        int MASK = R - 1;  // 255

        int N = a.length;
        int[] aux = new int[N];

        for (int d = 0; d < W; d++) {

            // compute frequency counts
            int[] count = new int[R + 1];
            for (int i : a) {
                int c = (i >> BITS_PER_BYTE * d) & MASK; // d-th byte number
                count[c + 1]++;
            }

            // compute cumulates
            for (int r = 0; r < R; r++) count[r + 1] += count[r];

            // for most significant byte, 0x80-0xFF comes before 0x00-0x7F
            if (d == W - 1) {
                int shift1 = count[R] - count[R / 2];
                int shift2 = count[R / 2];
                for (int r = 0; r < R / 2; r++) count[r] += shift1;
                for (int r = R / 2; r < R; r++) count[r] -= shift2;
            }

            // move data
            for (int i : a) {
                int c = (i >> BITS_PER_BYTE * d) & MASK;
                aux[count[c]++] = i;
            }

            // copy back
            System.arraycopy(aux, 0, a, 0, N);
        }
    }

    public static void main(String[] args) {
        String[] a = new String[]{"dab", "add", "cab", "fad", "fee", "bad", "dad", "bee", "fed", "bed", "ebb", "ace"};
        sort(a, 3);
        //System.out.println(Arrays.toString(a));
        System.out.println(1);
        System.out.println(Integer.toBinaryString(255));
        int[] is = new int[]{12, 13, -5, 32, 17, 2, -2, -7};
        sort(is);
        System.out.println(Arrays.toString(is));
        System.out.println(Integer.toBinaryString(-1));
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE));
    }
}