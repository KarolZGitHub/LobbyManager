package pl.coderslab.lobbymanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.lobbymanager.entity.Game;
import pl.coderslab.lobbymanager.entity.Role;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.GameRepository;
import pl.coderslab.lobbymanager.repository.RoleRepository;
import pl.coderslab.lobbymanager.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final GameRepository gameRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/register")
    public String registerUser(Model model) {
        List<Game> gameList = gameRepository.findAll();
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("gameList", gameList);
        return "register";
    }

    @PostMapping("/register")
    public String validateUsers(@Valid User user, BindingResult bindingResult, Model model) {
        user.setActive(true);
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER").get());
        user.setRoles(roles);

        if (bindingResult.hasErrors()) {
            return "register";
        }
        if (userService.saveUser(user, model)) {
            return "login";
        } else {
            return "register";
        }
    }
}