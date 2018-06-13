package strings;

/**
 * Reads in two strings, the pattern and the input text, and searches the
 * pattern in the input text using the bad-character rule part of the
 * Boyer-Moore algorithm. (does not implement the strong good suffix rule)
 */
public class BoyerMoore {
    private int[] right; // the bad-character skip array
    private int R;  // the radix
    private String pat;  // the pattern

    public BoyerMoore(String pat) {
        this.R = 256;
        this.pat = pat;

        right = new int[R];
        // position of rightmost occurrence of c in the patterns
        for (int c = 0; c < R; c++) right[c] = -1;
        for (int j = 0; j < pat.length(); j++) right[pat.charAt(j)] = j;
    }

    // return offset of first match; N if no match
    public int search(String txt) {
        int M = pat.length();
        int N = txt.length();
        int skip;
        for (int i = 0; i <= N - M; i += skip) {
            skip = 0;
            for (int j = M - 1; j >= 0; j--) {
                if (pat.charAt(j) != txt.charAt(i + j)) {
                    skip = Math.max(1, j - right[txt.charAt(i + j)]);
                    break;
                }
            }
            if (skip == 0) return i;  // found
        }
        return N;  // not found
    }

    public static void main(String[] args) {
        String pattern = "abracadabra";
        String text = "abacadabrabracabracadabrabrabracad";
        BoyerMoore boyerMoore = new BoyerMoore(pattern);
        int offset = boyerMoore.search(text);
        System.out.println("offset: " + offset);
    }
}