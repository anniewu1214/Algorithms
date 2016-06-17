package strings;

import java.util.Arrays;

public class Quick3string {

    private static final int CUTOFF = 15; // cutoff to insertion sort

    // sort the array of strings
    private static void sort(String[] a) {
        sort(a, 0, a.length - 1, 0);
    }

    // 3-way string quick-sort a[lo..hi] starting at dth character
    private static void sort(String[] a, int lo, int hi, int d) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(a, lo, lt - 1, d);
        if (v > 0) sort(a, lt, gt, d + 1);
        sort(a, gt + 1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--)
                exch(a, j, j - 1);
    }

    // is v less than w, starting at character d
    private static boolean less(String v, String w, int d) {
        return v.substring(d).compareTo(w.substring(d)) < 0;
    }

    // exchange a[i] and a[j]
    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // return the dth character of s, -1 if d = length of s
    private static int charAt(String s, int d) {
        if (d < s.length()) return s.charAt(d);
        else return -1;
    }

    public static void main(String[] args) {
        String[] strings = new String[]{"she", "sells", "seashells", "by", "the", "sea", "shore", "the", "shells", "she", "sells", "are", "surely", "seashells"};
        sort(strings);
        System.out.println(Arrays.toString(strings));
    }
}