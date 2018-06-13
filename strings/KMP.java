package strings;

/**
 * Reads in two strings, the pattern and the input text, and
 * searches for the pattern in the input text using the
 * KMP algorithm.
 */
public class KMP {
    private int[][] dfa;  // the KMP automoton
    private String pat;  // the searching pattern

    // Create the DFA from a String
    public KMP(String pat) {
        this.pat = pat;
        int M = pat.length();
        int R = 256;  // the radix
        dfa = new int[R][M];
        dfa[pat.charAt(0)][0] = 1;
        for (int X = 0, j = 1; j < M; j++) {
            for (int c = 0; c < R; c++)
                dfa[c][j] = dfa[c][X];  // Copy mismatch cases
            dfa[pat.charAt(j)][j] = j + 1;  // override match case
            X = dfa[pat.charAt(j)][X];  // update restart state
        }
    }

    // return ofset of first match; N if no match
    public int search(String txt) {
        int M = pat.length();
        int N = txt.length();
        int i, j;
        // simulate operation on DFA on text
        for (i = 0, j = 0; i < N && j < M; i++)
            j = dfa[txt.charAt(i)][j];
        if (j == M) return i - M;
        return N;
    }

    public static void main(String[] args) {
        String pattern = "abracadabra";
        String text = "abacadabrabracabracadabrabrabracad";
        KMP kmp = new KMP(pattern);
        int offset = kmp.search(text);
        System.out.println("offset: " + offset);
    }
}