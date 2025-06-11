package Server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ServerController {

    @RequestMapping("/menu")
    public String menu() {
        return "menu";
    }
}
