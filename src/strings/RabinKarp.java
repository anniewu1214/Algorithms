package strings;

import java.math.BigInteger;
import java.util.Random;

/**
 * Sub-string search using the Rabin-Karp algorithm.
 */
public class RabinKarp {
    private String pat;      // the pattern  // needed only for Las Vegas
    private long patHash;    // pattern hash value
    private int M;           // pattern length
    private long Q;          // a large prime, small enough to avoid long overflow
    private int R;           // radix
    private long RM;         // R^(M-1) % Q

    public RabinKarp(String pat) {
        this.pat = pat;      // save pattern (needed only for Las Vegas)
        R = 256;
        M = pat.length();
        Q = longRandomPrime();

        RM = 1; // pre-compute R^(M-1) % Q for use in removing leading digit
        for (int i = 1; i <= M - 1; i++) RM = (R * RM) % Q;
        patHash = hash(pat, M);
    }

    // Compute hash for key[0..M-1].
    private long hash(String key, int M) {
        long h = 0;
        for (int j = 0; j < M; j++) h = (R * h + key.charAt(j)) % Q;
        return h;
    }

    // Las Vegas version: does pat[] match txt[i..i-M+1] ?
    private boolean check(String txt, int i) {
        for (int j = 0; j < M; j++)
            if (pat.charAt(j) != txt.charAt(i + j))
                return false;
        return true;
    }

    // Monte Carlo version: always return true
    private boolean check(int i) {
        return true;
    }

    // check for exact match
    public int search(String txt) {
        int N = txt.length();
        if (N < M) return N;
        long txtHash = hash(txt, M);

        // check for match at offset 0
        if ((patHash == txtHash) && check(txt, 0)) return 0;

        // check for hash match; if hash match, check for exact match
        for (int i = M; i < N; i++) {
            // Remove leading digit, add trailing digit, check for match.
            txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q;
            txtHash = (txtHash * R + txt.charAt(i)) % Q;

            int offset = i - M + 1;
            if ((patHash == txtHash) && check(txt, offset)) return offset; // match
        }

        return N;  // no match
    }

    // a random 31-bit prime
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
    }

    public static void main(String[] args) {
        String pattern = "abracadabra";
        String text = "abacadabrabracabracadabrabrabracad";
        RabinKarp rabinKarp = new RabinKarp(pattern);
        int offset = rabinKarp.search(text);
        System.out.println("offset: " + offset);
    }
}