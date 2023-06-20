package pl.coderslab.lobbymanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
    @GetMapping("/adminOnly")
    public String showAdmin() {
        return "Only admin";
    }
}