package Server;

import DataGenerator.DataGeneratorMain;
import FlowNetwork.FlowNetworkMain;
import WordSearch.Menu;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class ServerController {
    private boolean dataIsEmpty = true;
    private int choice; //1 - word in the text, 2 - word in logs, 3 - word in own file

    @RequestMapping("/menu")
    public String menu(Model model) {
        UserChoice userChoice = new UserChoice();
        model.addAttribute("userChoice", userChoice);
        return "menu";
    }

    @RequestMapping("/wordSearchForm")
    public String wordSearchForm(Model model) {
        UserChoice userChoice = new UserChoice();
        model.addAttribute("userChoice", userChoice);
        return "wordSearchForm";
    }

    @PostMapping("/sent")
    public String sent(@ModelAttribute UserChoice userChoice, Model model) throws IOException {
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
            case "Search words in text" -> "WordSearchForm";
            case "Show logs" -> "showLogs";
            case "Provide text" -> {
                UserWordToSearch userWordToSearch = new UserWordToSearch();
                userWordToSearch.setType(1);
                model.addAttribute("userWordToSearch", userWordToSearch);
                yield "provideTextForm";
            }
            case "Search words in logs" -> {
                UserWordToSearch userWordToSearch = new UserWordToSearch();
                userWordToSearch.setType(2);
                model.addAttribute("userWordToSearch", userWordToSearch);
                yield "provideWordToSearchInLogsForm";
            }
            case "Search words in your own file" -> {
                UserWordToSearch userWordToSearch = new UserWordToSearch();
                userWordToSearch.setType(3);
                model.addAttribute("userWordToSearch", userWordToSearch);
                yield "provideInfoToSearchInYourOwnFileForm";
            }
            default -> "error";
        };
    }

    @PostMapping("/processText")
    public String processText(@ModelAttribute UserWordToSearch userWordToSearch, Model model) throws IOException {
         return switch (userWordToSearch.getType()) {
            case 1 -> {
                String option = userWordToSearch.getAlgorithmChoice();
                Menu menu = new Menu(userWordToSearch.getText(), option);
                menu.move("2", userWordToSearch.getWord());
                model.addAttribute("result", menu.getAlg().getResult().getResult(option));
                model.addAttribute("text", userWordToSearch.getText());
                yield "wordSearchResult";
            }
             default -> "error";
         };
    }
}
