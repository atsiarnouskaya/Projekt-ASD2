import java.util.Scanner;

public class Menu {
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
