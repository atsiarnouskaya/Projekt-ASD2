import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
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
