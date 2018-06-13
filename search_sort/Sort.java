package search_sort;

import helper.StdRandom;

import java.util.Arrays;
import java.util.Comparator;
// search_sort.Sort an array in ascending order.
public class Sort {
    private static final int CUTOFF = 7;

    private Sort() {
    }// This class should not be instantiated


    /**
     * Selection sort , using the natural order or a comparator.
     * Time complexity: O(n2)
     */
    public static void selectionSort(Comparable[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (less(a[j], a[min])) min = j;
            }
            swap(a, i, min);
        }
    }

    public static void selectionSort(Object[] a, Comparator c) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int iMin = i;
            for (int j = i + 1; j < n; j++) {
                if (less(a[j], a[iMin], c)) iMin = j;
            }
            swap(a, i, iMin);
        }
    }

    /**
     * Bubble sort, using the natural order.
     * Time complexity: O(n2)
     */
    public static void bubbleSort(Comparable[] a) {
        int n = a.length;
        for (int k = 0; k < n - 1; k++) {
            boolean sort = false;
            for (int i = 0; i < n - k - 1; i++) {
                if (less(a[i + 1], a[i])) {
                    swap(a, i, i + 1);
                    sort = true;
                }
            }
            if (!sort) break;
        }
    }

    /**
     * Insertion sort , using the natural order.
     * Time complexity: O(n2), but more efficient than bubble and selection sort.
     */
    public static void insertionSort(Comparable[] a) {
        int n = a.length;
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                swap(a, j, j - 1);
            }
        }
    }

    public static void insertionSort(Comparable[] a, int lo, int hi) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > 0 && less(a[j], a[j - 1]); j--) {
                swap(a, j, j - 1);
            }
        }
    }

    /**
     * Merge sort , using the natural order.
     * Time complexity: O(nlogn), space complexity: O(n)
     */
    public static void mergeSort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        mergeSort(a, aux, 0, a.length - 1);
    }

    private static void mergeSort(Comparable[] a, Comparable[] aux, int lo, int hi) {
        // if (hi <= lo) return;
        // Improvement: Use insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertionSort(a, lo, hi);
            return;
        }
        int mid = lo + (hi - lo) / 2;
        mergeSort(a, aux, lo, mid);
        mergeSort(a, aux, mid + 1, hi);

        // Improvement: Test whether array is already in order.
        if (!less(a[mid + 1], a[mid])) return;

        merge(a, aux, lo, mid, hi);
    }

    private static void mergeSortBU(Comparable[] a) {
        int N = a.length;
        Comparable[] aux = new Comparable[N];
        for (int n = 1; n < N; n *= 2) { // n is the length of slice: 1, 2, 4, 8, ...
            for (int i = 0; i < N - n; i += n * 2) {
                int lo = i;
                int m = i + n - 1;
                int hi = Math.min(i + n + n - 1, N - 1);
                merge(a, aux, lo, m, hi);
            }
        }
    }

    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        // copies everything to an auxiliary array and then merges back to the original
        System.arraycopy(a, lo, aux, lo, hi + 1 - lo);
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    /**
     * Quick sort , using the natural order.
     * Time complexity: O(nlogn), space complexity: O(nlogn), not stable
     */
    public static void quickSort(Comparable[] a) {
        // shuffle needed for performance guarantee
        quickSort(a, 0, a.length - 1);
    }

    public static void quickSort(Comparable[] a, int lo, int hi) {
        //if (hi <= lo) return;
        // Improvement: insertion sort small subarrays
        if (hi <= lo + CUTOFF - 1) {
            insertionSort(a, lo, hi);
            return;
        }

        // Improvement: best choice of pivot item = median, estimate by sampling
        int m = median3(a, lo, lo + (hi - lo) / 2, hi);
        swap(a, lo, m);

        int pIndex = partition(a, lo, hi);
        quickSort(a, lo, pIndex - 1);
        quickSort(a, pIndex + 1, hi);
    }

    // return the index of the median element among a[i], a[j], and a[k]
    private static int median3(Comparable[] a, int i, int j, int k) {
        return (less(a[i], a[j]) ?
                (less(a[j], a[k]) ? j : less(a[i], a[k]) ? k : i) :
                (less(a[k], a[j]) ? j : less(a[k], a[i]) ? k : i));
    }


    // 3-way quick sort, entropy-optimal, solve most of real problem in linear time
    public static void quickSort3Way(Comparable[] a) {
        StdRandom.shuffle(a);
        quickSort3Way(a, 0, a.length - 1);
    }

    public static void quickSort3Way(Comparable[] a, int lo, int hi) {
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        Comparable v = a[lo];
        int i = lo;
        while (i <= gt) {
            int cmp = a[i].compareTo(v);
            if (cmp < 0) swap(a, lt++, i++);
            else if (cmp > 0) swap(a, i, gt--);
            else i++;
        }
        quickSort3Way(a, lo, lt - 1);
        quickSort3Way(a, gt + 1, hi);
    }

    // partitioning in place
    private static int partition(Comparable[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        while (true) {
            while (less(a[++i], a[lo])) // scan i from left to right so long as a[i] < a[lo]
                if (i == hi) break;

            while (less(a[lo], a[--j])) // scan j from right to left so long as a[j] > a[lo]
                if (j == lo) break;

            if (i >= j) break;  // repeat until pointers cross: this is tricky
            swap(a, i, j);  // exchange a[i] with a[j]
        }
        swap(a, lo, j);
        return j;
    }

    // Application: quick-select, rearranges the array so that a[k] contains the kth smallest key;
    public static Comparable select(Comparable[] a, int k) {
        if (k < 0 || k >= a.length) throw new IndexOutOfBoundsException("Selected element out of bounds");
        StdRandom.shuffle(a);

        int lo = 0, hi = a.length - 1;
        while (hi > lo) {
            int i = partition(a, lo, hi);
            if (i > k) hi = i - 1;
            else if (i < k) lo = i + 1;
            else return a[i];
        }
        return a[lo];
    }


    // Heap sort
    public static void heapSort(Comparable[] pq) {
        int N = pq.length;
        for (int k = N / 2; k >= 1; k--) sink(pq, k, N); // heap construction
        while (N > 1) {
            swapHeap(pq, 1, N--);
            sink(pq, 1, N);
        }
    }

    private static void sink(Comparable[] pq, int k, int N) {
        while (2 * k <= N) {
            int j = 2 * k;
            if (j < N && lessHeap(pq, j, j + 1)) j++;
            if (!lessHeap(pq, k, j)) break;
            swapHeap(pq, k, j);
            k = j;
        }
    }

    /***********************************************************************
     * Helper functions for comparisons and swaps.
     * Indices are "off-by-one" to support 1-based indexing.
     **********************************************************************/
    private static boolean lessHeap(Comparable[] pq, int i, int j) {
        return pq[i - 1].compareTo(pq[j - 1]) < 0;
    }

    private static void swapHeap(Object[] pq, int i, int j) {
        Object swap = pq[i - 1];
        pq[i - 1] = pq[j - 1];
        pq[j - 1] = swap;
    }


    /**
     * Helper function for sorting
     */
    // is c1 < c2 ?
    private static boolean less(Comparable c1, Comparable c2) {
        return c1.compareTo(c2) < 0;
    }

    // is o1 < o2 ?
    private static boolean less(Object o1, Object o2, Comparator c) {
        return c.compare(o1, o2) < 0;
    }

    // Swap a[i] and a[j]
    private static void swap(Object[] a, int i, int j) {
        Object temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public static void main(String[] args) {
        Integer[] ints = {8, 5, 2, 6, 1, 7, 0, 9, 4, 3};
        // selectionSort(ints);
        //bubbleSort(ints);
        //insertionSort(ints);
        //mergeSort(ints);
        //quickSort(ints);
        heapSort(ints);
        System.out.println(Arrays.toString(ints));
    }
}