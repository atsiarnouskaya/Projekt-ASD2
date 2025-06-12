package Server;

import DataGenerator.DataGeneratorMain;
import FlowNetwork.FlowNetworkMain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class ServerController {
    private boolean dataIsEmpty = true;

    @RequestMapping("/menu")
    public String menu(Model model) {
        UserChoice userChoice = new UserChoice();
        model.addAttribute("userChoice", userChoice);
        return "menu";
    }

    @PostMapping("/sent")
    public String sent(@ModelAttribute UserChoice userChoice) throws IOException {
        return switch (userChoice.getChoice()) {
            case "Generate new map" -> {
                DataGeneratorMain.run();
                System.out.println("Generated data");
                dataIsEmpty = false;
                yield "success";
            }
            case "Calculate flows" -> {
                if (dataIsEmpty) {
                    DataGeneratorMain.run();
                    System.out.println("Generated data");
                }
                FlowNetworkMain.run();
                yield "redirect:/graph.html";
            }
            case "Show logs" -> "/showLogs";
            case "Find words in logs" -> "/findWordsInLogs";
            default -> "/error";
        };
    }
}
