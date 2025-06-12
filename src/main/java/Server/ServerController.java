package Server;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ServerController {

    @RequestMapping("/menu")
    public String menu(Model model) {
        UserChoice userChoice = new UserChoice();
        model.addAttribute("userChoice", userChoice);
        return "menu";
    }

    @PostMapping("/sent")
    public String sent(@ModelAttribute UserChoice userChoice) {
        return switch (userChoice.getChoice()) {
            case "Generate new map" -> "settingUpMap";
            case "Calculate flows" -> "redirect:/graph.html";
            case "Show logs" -> "showLogs";
            case "Find words in logs" -> "findWordsInLogs";
            default -> "error";
        };

    }
}
