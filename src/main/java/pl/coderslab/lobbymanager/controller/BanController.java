package pl.coderslab.lobbymanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BanController {
    @GetMapping("/banned")
    public String showBanInformation() {
        return "banned";
    }
}
