package strings;

import helper.StdOut;

/**
 * A data type for alphabets, for use with string-processing code
 * that must convert between an alphabet of size R and the integers
 * 0 through R-1.
 */
public class Alphabet {
    public static final Alphabet BINARY = new Alphabet("01");
    public static final Alphabet OCTAL = new Alphabet("01234567");
    public static final Alphabet DECIMAL = new Alphabet("0123456789");
    public static final Alphabet HEXADECIMAL = new Alphabet("0123456789ABCDEF");
    public static final Alphabet DNA = new Alphabet("ACTG");
    public static final Alphabet LOWERCASE = new Alphabet("abcdefghijklmnopqrstuvwxyz");
    public static final Alphabet UPPERCASE = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public static final Alphabet PROTEIN = new Alphabet("ACDEFGHIKLMNPQRSTVWY");
    public static final Alphabet BASE64 = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/");
    public static final Alphabet ASCII = new Alphabet(128);
    public static final Alphabet EXTENDED_ASCII = new Alphabet(256);
    public static final Alphabet UNICODE16 = new Alphabet(65536);

    private char[] alphabet; // the chars in the alphabet
    private int[] inverse;  // indices
    private int R; // the radix of the alphabet

    /**
     * Initialize a new alphabet from the given set of chars.
     *
     * @param alpha the set of chars
     */
    public Alphabet(String alpha) {
        // check that alphabet contains no duplicate chars
        boolean[] unicode = new boolean[Character.MAX_VALUE];
        for (int i = 0; i < alpha.length(); i++) {
            char c = alpha.charAt(i);
            if (unicode[c]) throw new IllegalArgumentException("Illegal alphabet: repeated character = '" + c + "'");
            unicode[c] = true;
        }

        alphabet = alpha.toCharArray();
        R = alpha.length();
        inverse = new int[Character.MAX_VALUE];
        for (int i = 0; i < inverse.length; i++) inverse[i] = -1;
        for (int c = 0; c < R; c++) inverse[alphabet[c]] = c;
    }

    /**
     * Initialize a new alphabet using chars 0 through R-1.
     *
     * @param R the number of chars in the alphabet (the radix)
     */
    private Alphabet(int R) {
        alphabet = new char[R];
        this.R = R;
        inverse = new int[R];

        for (int i = 0; i < R; i++) {
            alphabet[i] = (char) i;
            inverse[i] = i;
        }
    }

    /**
     * Initializes a new alphabet using chars 0 through 255.
     */
    public Alphabet() {
        this(256);
    }

    /**
     * Returns true if the char is in the alphabet.
     */
    public boolean contains(char c) {
        return inverse[c] != -1;
    }

    /**
     * Returns the number of chars in this alphabet.
     */
    public int R() {
        return R;
    }

    /**
     * Returns the binary logarithm of the number of chars in this alphabet.
     */
    public int lgR() {
        int lgR = 0;
        for (int t = R - 1; t >= 1; t /= 2)
            lgR++;
        return lgR;
    }

    /**
     * Returns the index corresponding to the argument character.
     */
    public int toIndex(char c) {
        if (c >= inverse.length || inverse[c] == -1)
            throw new IllegalArgumentException("Character " + c + " not in alphabet");
        return inverse[c];
    }

    /**
     * Returns the indices corresponding to the argument characters.
     */
    public int[] toIndices(String s) {
        char[] source = s.toCharArray();
        int[] target = new int[s.length()];
        for (int i = 0; i < source.length; i++) target[i] = toIndex(source[i]);
        return target;
    }

    /**
     * Returns the character corresponding to the argument index.
     */
    public char toChar(int index) {
        if (index < 0 || index >= R) throw new IndexOutOfBoundsException("Alphabet index out of bounds");
        return alphabet[index];
    }

    /**
     * Returns the characters corresponding to the argument indices.
     */
    public String toChars(int[] indices) {
        StringBuilder s = new StringBuilder(indices.length);
        for (int index : indices) s.append(toChar(index));
        return s.toString();
    }

    public static void main(String[] args) {
        int[] encoded1 = Alphabet.BASE64.toIndices("NowIsTheTimeForAllGoodMen");
        String decoded1 = Alphabet.BASE64.toChars(encoded1);
        StdOut.println(decoded1);

        int[]  encoded2 = Alphabet.DNA.toIndices("AACGAACGGTTTACCCCG");
        String decoded2 = Alphabet.DNA.toChars(encoded2);
        StdOut.println(decoded2);

        int[]  encoded3 = Alphabet.DECIMAL.toIndices("01234567890123456789");
        String decoded3 = Alphabet.DECIMAL.toChars(encoded3);
        StdOut.println(decoded3);
    }
}