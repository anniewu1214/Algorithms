package strings;

/**
 * An optimized version of KMP
 */
public class KMPplus {
    private String pattern;
    private int[] next;

    // create Knuth-Morris-Pratt NFA from pattern
    public KMPplus(String pattern) {
        this.pattern = pattern;
        int M = pattern.length();
        next = new int[M];
        int j = -1;
        for (int i = 0; i < M; i++) {
            if (i == 0) next[i] = -1;
            else if (pattern.charAt(i) != pattern.charAt(j)) next[i] = j;
            else next[i] = next[j];
            while (j >= 0 && pattern.charAt(i) != pattern.charAt(j)) {
                j = next[j];
            }
            j++;
        }

        for (int i = 0; i < M; i++)
            System.out.println("next[" + i + "] = " + next[i]);
    }

    // return offset of first occurrence of text in pattern (or N if no match)
    // simulate the NFA to find match
    public int search(String text) {
        int M = pattern.length();
        int N = text.length();
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            while (j >= 0 && text.charAt(i) != pattern.charAt(j))
                j = next[j];
            j++;
        }
        if (j == M) return i - M;
        return N;
    }


    // test client
    public static void main(String[] args) {
        String pattern = "abracadabra";
        String text = "abacadabrabracabracadabrabrabracad";
        KMPplus kmp = new KMPplus(pattern);
        int offset = kmp.search(text);
        System.out.println("offset: " + offset);
    }
}