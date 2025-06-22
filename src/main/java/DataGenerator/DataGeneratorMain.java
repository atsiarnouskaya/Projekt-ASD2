package DataGenerator;

import java.io.*;
import FlowNetwork.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataGeneratorMain {
    private static final FileWriter writer;

    static {
        try {
            writer = new FileWriter("src/logs/output.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run() throws IOException {
        Generator generator = new Generator(2, 5, 4, 2, 4);
        Data data = generator.generate();
        writer.write("[OK]  Data is generated without errors\n");
        writer.flush();
        generateJSONfile(data);
        writer.write("[OK]  Data is saved to JSON file without errors\n");
        writer.flush();
    }

    public static void run(int seed, int roadsCount, int farmlandsCount, int breweriesCount, int tavernsCount) throws IOException {
        Generator generator = new Generator(seed, roadsCount, farmlandsCount, breweriesCount, tavernsCount);
        Data data = generator.generate();
        writer.write("[OK]  Data is generated without errors\n");
        writer.flush();
        generateJSONfile(data);
        writer.write("[OK]  Data is saved to JSON file without errors\n");
        writer.flush();
    }

    public static void generateJSONfile(Data data) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.json"))) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String json = gson.toJson(data);
            writer.write(json);
        } catch (IOException e) {
            writer.write("An error occurred while saving to the file: " + e.getMessage() + "\n");
            writer.flush();
            System.err.println("An error occurred while saving to the file: " + e.getMessage());
        }
    }
}