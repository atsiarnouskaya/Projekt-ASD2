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
        switch (input) {
            case "0":
                list();
                break;
            case "1":
                writer.write("Searching in the file \n");
                writer.flush();

                ReadFile readFile = new ReadFile();
                String text = readFile.reading(file);
                if (text.isEmpty())
                    break;
                choiceAlgorithm(text, pattern, algorithmChoice);
                break;
            case "2":
                writer.write("Searching in the text \n");
                writer.flush();

                choiceAlgorithm(inputText, pattern, algorithmChoice);
                return true;
            default:
                writer.write("This option does not exist \n");
                writer.flush();
        }
        return true;
    }

    public void choiceAlgorithm(String text,  String pattern, String algorithmChoice) throws IOException {
        boolean cycle = true;
        while (cycle) {
            if (pattern.isEmpty()){
                writer.write("Pattern is empty \n");
                writer.flush();

                break;
            }
            switch (algorithmChoice) {
                case "1":
                    writer.write("Knuth-Morris-Pratt algorithm was chosen \n");
                    writer.flush();

                    alg.KMP(text, pattern);
                    return;
                case "2":
                    writer.write("Boyer-Moore algorithm was chosen \n");
                    writer.flush();

                    alg.BM(text, pattern);
                    return;
                case "3":
                    writer.write("Both algorithms were chosen \n");
                    writer.flush();

                    alg.KMP(text, pattern);
                    alg.BM(text, pattern);
                    return;
                default:
                    writer.write("This option does not exist \n");
                    writer.flush();
            }
        }
    }
}
