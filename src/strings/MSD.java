package strings;

import java.util.Arrays;

public class MSD {
    private static final int R = 256;  // extended ASCII alphabet size
    private static final int CUTOFF = 15;  // cut off to insertion sort

    // sort array of strings
    private static void sort(String[] a) {
        String[] aux = new String[a.length];
        sort(a, aux, 0, a.length - 1, 0);
    }

    private static void sort(String[] a, String[] aux, int lo, int hi, int d) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        // compute frequency counts
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) count[charAt(a[i], d) + 2]++;
        // transform counts to indices
        for (int r = 0; r < R + 1; r++) count[r + 1] += count[r];
        // distribute
        for (int i = lo; i <= hi; i++) aux[count[charAt(a[i], d) + 1]++] = a[i];
        // copy back
        System.arraycopy(aux, 0, a, lo, hi + 1 - lo);
        // recursively sort for each character
        for (int r = 0; r < R; r++) sort(a, aux, lo + count[r], lo + count[r + 1] - 1, d + 1);
    }

    // insertion sort a[lo..hi], starting at dth character
    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--)
                exch(a, j, j - 1);
    }

    // exchange a[i] and a[j]
    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(String v, String w, int d) {
        return v.substring(d).compareTo(w.substring(d)) < 0;
    }

    // return dth character of s, -1 if d = length of string
    private static int charAt(String s, int i) {
        if (i < s.length()) return s.charAt(i);
        else return -1;
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"she", "sells", "seashells", "by", "the", "sea", "shore", "the", "shells", "she", "sells", "are", "surely", "seashells"};
        sort(strings);
        System.out.println(Arrays.toString(strings));
    }
}