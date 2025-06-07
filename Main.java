import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class ReadFile {
    public String reading(String name) {
        String text = "";
        try {
            File myObj = new File(name);
            Scanner myReader = new Scanner(myObj);
            StringBuilder sb = new StringBuilder();
            while (myReader.hasNextLine()) {
                sb.append(myReader.nextLine()).append("\n");
            }
            text = sb.toString();
            myReader.close();
            if (text.isEmpty())
                System.out.println("File is empty");
        }
        catch (FileNotFoundException e) {
            System.out.println("File with such name does not exist");
        }
        return text;
    }
}

class Menu {
    private final Scanner scanner = new Scanner(System.in);

    public void list() {
        System.out.println("Menu:\n" +
                "1. Search for pattern in file;\n" +
                "2. Search for pattern in your own text;\n" +
                "3. Exit;");
    }

    public boolean move() {
        System.out.println("Enter action: (0 - Show menu)");
        String input = scanner.nextLine().trim();
        switch (input) {
            case "0":
                list();
                break;
            case "1":
                System.out.println("Enter file name: ");
                String name = scanner.nextLine();
                ReadFile file = new ReadFile();
                String text = file.reading(name);
                if (text.isEmpty())
                    break;
                choiceAlgorithm(text);
                break;
            case "2":
                System.out.println("Enter your own text: ");
                String nText = scanner.nextLine();
                choiceAlgorithm(nText);
                break;
            case "3":
                System.out.println("Have a nice day !");
                return false;
            default:
                System.out.println("This option does not exist");
        }
        return true;
    }

    public void choiceAlgorithm(String text) {
        boolean cycle = true;
        while (cycle) {
            System.out.println("Enter pattern: ");
            String pattern = scanner.nextLine();
            if (pattern.isEmpty()){
                System.out.println("Pattern is empty");
                break;
            }
            System.out.println("Choose which algorithm you want to use:\n" +
                    "1. KMP;\n" +
                    "2. BM;\n" +
                    "3. Both;");

            String input = scanner.nextLine().trim();
            Algorithms alg = new Algorithms();
            switch (input) {
                case "1":
                    System.out.println("Knuth-Morris-Pratt algorithm:");
                    alg.KMP(text, pattern);
                    break;
                case "2":
                    System.out.println("Boyer-Moore algorithm:");
                    alg.BM(text, pattern);
                    break;
                case "3":
                    System.out.println("Knuth-Morris-Pratt algorithm:");
                    alg.KMP(text, pattern);
                    System.out.println("*****");
                    System.out.println("Boyer-Moore algorithm:");
                    alg.BM(text, pattern);
                    break;
                default:
                    System.out.println("This option does not exist");
            }
            System.out.println("_____\n" +
                    "Choose next action:\n" +
                    "0. Menu;\n" +
                    "1. Change pattern and continue working with this text");
            String output = scanner.nextLine();
            if (output.equals("0")) {
                list();
                cycle = false;
            }
        }
    }
}

class Algorithms {
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
                q = pi[q - 1];
                exist = true;
            }
        }
        if (!exist)
            System.out.println("Pattern does not occur");
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
                i++;
            }
        }
        if (!exist)
            System.out.println("Pattern does not occur");
    }
}

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        boolean proceed = true;
        menu.list();
        while (proceed) {
            proceed = menu.move();
        }
    }
}


