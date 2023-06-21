package pl.coderslab.lobbymanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UserRepository userRepository;

    // http://localhost:8080/users/allUsers
    @GetMapping("/allUsers")
    public String showAllUsers(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("userList", userList);
        return "allUsers";
    }
}