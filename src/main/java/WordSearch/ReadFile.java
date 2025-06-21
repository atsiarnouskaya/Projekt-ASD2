package WordSearch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ReadFile {
    private static final FileWriter writer;

    static {
        try {
            writer = new FileWriter("src/logs/output.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String reading(File file) throws IOException {
    //public String reading(String name) {
        writer.write("Started reading a file");
        writer.flush();
        String text = "";
        try {
//            File myObj = new File(name);
//            Scanner myReader = new Scanner(myObj);
            Scanner myReader = new Scanner(file);
            StringBuilder sb = new StringBuilder();
            while (myReader.hasNextLine()) {
                sb.append(myReader.nextLine()).append("\n");
            }
            text = sb.toString();
            myReader.close();
            if (text.isEmpty()) {
                writer.write("File is empty");
                writer.flush();
                System.out.println("File is empty");
            }
        }
        catch (FileNotFoundException e) {
            writer.write("File with such name does not exist");
            writer.flush();
            System.out.println("File with such name does not exist");
        }
        writer.write("File is read");
        writer.flush();
        return text;
    }
}
