package Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class StartServer {
    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("src/logs/output.txt", false);
        fw.write("[OK]  Server started\n");
        fw.close();
        SpringApplication.run(StartServer.class, args);
    }
}
