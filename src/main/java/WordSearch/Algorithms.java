import lombok.Getter;

import java.io.FileWriter;
import java.io.IOException;

@Getter
public class Algorithms {
    private final Result result = new Result();
    private static final FileWriter writer;

    static {
        try {
            writer = new FileWriter("src/logs/output.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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

    public int[] createBMNext(String pattern) {
        int[] PI = new int[pattern.length() + 1];
        int[] BMNext = new int[pattern.length() + 1];
        int i = pattern.length();
        int b = pattern.length() + 1;
        PI[i] = b;
        while (i > 0) {
            while (b <= pattern.length() && pattern.charAt(i - 1) != pattern.charAt(b - 1)) {
                if (BMNext[b] == 0) {
                    BMNext[b] = b - i;
                }
                b = PI[b];
            }
            i--;
            b--;
            PI[i] = b;
        }
        b = PI[0];
        for (i = 0; i <= pattern.length(); i++) {
            if (BMNext[i] == 0)
                BMNext[i] = b;
            if (i == b)
                b = PI[b];
        }
        return BMNext;
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

    public void KMP(String text, String pattern) throws IOException {
        writer.write("Started KMP algorithm \n");
        writer.flush();

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
                writer.write("Pattern occurs in the text \n");
                writer.flush();

                System.out.println("Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + (i - pattern.length() + 1));
                result.addKMPResult("\n[KMP] Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + (i - pattern.length() + 1));
                q = pi[q - 1];
                exist = true;
            }
        }
        if (!exist) {
            writer.write("Pattern does not occur in the text \n");
            writer.flush();
            System.out.println("Pattern does not occur");
            result.addKMPResult("\n[KMP] Pattern does not occur");
        }
    }

    public void BM(String text, String pattern) throws IOException {
        writer.write("Started BM algorithm \n");
        writer.flush();
        int[] LAST = createLAST(pattern);
        int[] BMNext = createBMNext(pattern);
        boolean exist = false;
        int i = 0;
        while (i <= text.length() - pattern.length()) {
            int j = pattern.length() - 1;
            while (j >= 0 && pattern.charAt(j) == text.charAt(i + j)) {
                j--;
            }
            if (j >= 0)
                i += Math.max(BMNext[j + 1], j - LAST[text.charAt(i + j)]);

            else {
                writer.write("Pattern occurs in the text \n");
                writer.flush();
                exist = true;
                System.out.println("Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + i);
                result.addBMResult("\n[BM] Pattern \"" + pattern + "\" with length " + pattern.length() + " occurs at position " + i);
                i += BMNext[0];
            }
        }
        if (!exist) {
            writer.write("Pattern does not occur in the text \n");
            writer.flush();
            System.out.println("Pattern does not occur");
            result.addBMResult("\n[BM] Pattern does not occur");
        }
    }
}