package WordSearch;

import lombok.Getter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Getter
public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final File file;
    private final String inputText;
    private final String algorithmChoice;
    private final Algorithms alg = new Algorithms();
    private static final FileWriter writer;

    static {
        try {
            writer = new FileWriter("src/logs/output.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Menu(File inputFile, String algorithmChoice) {
       this.file = inputFile;
       this.inputText = "";
       this.algorithmChoice = algorithmChoice;
    }

    public Menu(String inputText, String algorithmChoice) {
        this.inputText = inputText;
        this.file = null;
        this.algorithmChoice = algorithmChoice;
    }

    public void list() {
        System.out.println("Menu:\n" +
                "1. Search for pattern in file;\n" +
                "2. Search for pattern in your own text;\n" +
                "3. Exit;");
    }

    public boolean move(String input, String pattern) throws IOException {
        System.out.println("Enter action: (0 - Show menu)");
        //String input = scanner.nextLine().trim();
        switch (input) {
            case "0":
                list();
                break;
            case "1":
//                System.out.println("Enter file name: ");
//                String name = scanner.nextLine();
//                ReadFile file = new ReadFile();
                writer.write("Searching in the file \n");
                writer.flush();
                ReadFile readFile = new ReadFile();
                String text = readFile.reading(file);
                if (text.isEmpty())
                    break;
                choiceAlgorithm(text, pattern, algorithmChoice);
                break;
            case "2":
//                System.out.println("Enter your own text: ");
//                String nText = scanner.nextLine();
                writer.write("Searching in the text \n");
                writer.flush();
                choiceAlgorithm(inputText, pattern, algorithmChoice);
                return true;
            case "3":
                System.out.println("Have a nice day !");
                return false;
            default:
                System.out.println("This option does not exist");
        }
        return true;
    }

    public void choiceAlgorithm(String text,  String pattern, String algorithmChoice) throws IOException {
        boolean cycle = true;
        while (cycle) {
//            System.out.println("Enter pattern: ");
//            String pattern = scanner.nextLine();
            if (pattern.isEmpty()){
                System.out.println("Pattern is empty");
                break;
            }
            System.out.println("Choose which algorithm you want to use:\n" +
                    "1. KMP;\n" +
                    "2. BM;\n" +
                    "3. Both;");

//            String input = scanner.nextLine().trim();
//            Algorithms alg = new Algorithms();
            String input = algorithmChoice;
            switch (input) {
                case "1":
                    writer.write("Knuth-Morris-Pratt algorithm was chosen \n");
                    writer.flush();
                    System.out.println("Knuth-Morris-Pratt algorithm:");
                    alg.KMP(text, pattern);
                    //break;
                    return;
                case "2":
                    System.out.println("Boyer-Moore algorithm:");
                    writer.write("Boyer-Moore algorithm was chosen \n");
                    writer.flush();
                    alg.BM(text, pattern);
                    //break;
                    return;
                case "3":
                    writer.write("Both algorithms were chosen \n");
                    writer.flush();
                    System.out.println("Knuth-Morris-Pratt algorithm:");
                    alg.KMP(text, pattern);
                    System.out.println("*****");
                    System.out.println("Boyer-Moore algorithm:");
                    alg.BM(text, pattern);
                    //break;
                    return;
                default:
                    System.out.println("This option does not exist");
            }
            System.out.println("_____\n" +
                    "Choose next action:\n" +
                    "0. Menu;\n" +
                    "1. Change pattern and continue working with this text");
            String output = scanner.nextLine();
            switch (output) {
                case "0":
                    list();
                    cycle = false;
                    break;
                case "1":
                    break;
                default:
                    System.out.println("This option does not exist");
                    return;
            }
        }
    }
}
