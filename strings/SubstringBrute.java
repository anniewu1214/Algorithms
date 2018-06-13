package strings;

/**
 * Reads in two strings, the pattern and the input text, and
 * searches for the patterns in the input text using brute force.
 */
public class SubstringBrute {

    /***************************************************************************
     * String versions.
     ***************************************************************************/

    // return offset of first match or N if no match
    public static int search1(String pat, String txt) {
        int M = pat.length();
        int N = txt.length();

        for (int i = 0; i <= N - M; i++) {
            int j;
            for (j = 0; j < M; j++)
                if (txt.charAt(i + j) != pat.charAt(j)) break;
            if (j == M) return i;
        }
        return N;
    }

    // alternate implementation showing backup
    public static int search2(String pat, String txt) {
        int M = pat.length();
        int N = txt.length();

        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            if (txt.charAt(i) == pat.charAt(j)) j++;
            else {
                i -= j;
                j = 0;
            }
        }
        if (j == M) return i - M;
        else return N;
    }

    /***************************************************************************
     * char[] array versions.
     ***************************************************************************/

    // return offset of first match or N if no match
    public static int search1(char[] pattern, char[] text) {
        int M = pattern.length;
        int N = text.length;

        for (int i = 0; i < N - M; i++) {
            int j;
            for (j = 0; j < M; j++) if (text[i + j] != pattern[j]) break;
            if (j == M) return i;
        }
        return N;
    }

    // alternate implementation
    public static int search2(char[] pattern, char[] text) {
        int M = pattern.length;
        int N = text.length;
        int i, j;
        for (i = 0, j = 0; i < N && j < M; i++) {
            if (text[i] == pattern[j]) j++;
            else {
                i -= j;
                j = 0;
            }
        }
        if (j == M) return i - M;
        else return N;
    }

    public static void main(String[] args) {
        String pattern = "abracadabra";
        String text = "abacadabrabracabracadabrabrabracad";
        int offset1 = search1(pattern, text);
        int offset2 = search2(pattern, text);
        System.out.println(offset1 + " " + offset2);
    }
}