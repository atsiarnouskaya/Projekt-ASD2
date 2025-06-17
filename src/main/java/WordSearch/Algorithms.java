package WordSearch;

import lombok.Getter;

@Getter
public class Algorithms {
    private final Result result = new Result();
    public int[] createPi(String pattern){
        int[] pi = new int[pattern.length()];
        int k = 0;
        for (int i = 1; i < pattern.length(); i++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
                k = pi[k - 1];
            }
            if (pattern.charAt(k) == pattern.charAt(i)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }

    public int[] createLAST(String pattern){
        int AL = 128;
        int[] LAST = new int[AL];
        for (int i = 0; i < AL; i++)
            LAST[i] = -1;
        for (int i = 0; i < pattern.length(); i++)
            LAST[pattern.charAt(i)] = i;
        return LAST;
    }

    public void KMP(String text, String pattern) {
        int[] pi = createPi(pattern);
        int q = 0;
        boolean exist = false;
        for (int i = 0; i < text.length(); i++) {
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                q = pi[q - 1];
            }
            if (pattern.charAt(q) == text.charAt(i)) {
                q++;
            }
            if (q == pattern.length()) {
                System.out.println("Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + (i - pattern.length() + 1));
                result.addKMPResult("\n[KMP] Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + (i - pattern.length() + 1));
                q = pi[q - 1];
                exist = true;
            }
        }
        if (!exist) {
            System.out.println("Pattern does not occur");
            result.addKMPResult("\n[KMP] Pattern does not occur");
        }
    }

    public void BM(String text, String pattern) {
        int[] LAST = createLAST(pattern);
        boolean exist = false;
        int i = 0;
        while (i <= text.length() - pattern.length()) {
            int j = pattern.length() - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                j--;
            }
            if (j > -1)
                i += Math.max(1, j - LAST[text.charAt(i + j)]);

            else {
                exist = true;
                System.out.println("Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + i);
                result.addBMResult("\n[BM] Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + i);
                i++;
            }
        }
        if (!exist) {
            System.out.println("Pattern does not occur/automatic exit to main menu");
            result.addBMResult("\n[BM] Pattern does not occur/automatic exit");
        }
    }
}
