package pl.coderslab.lobbymanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.coderslab.lobbymanager.entity.Game;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.GameRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;
import pl.coderslab.lobbymanager.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;

    // http://localhost:8080/admin/adminOnly
    // shows all users, and options
    @GetMapping("/adminPanel")
    public String showAdminPanel(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("userList", userList);
        return "adminDashboard";
    }

    // http://localhost:8080/admin/editUser
    // editing user in database by admin
    @GetMapping("/editUser")
    public String editUser(@RequestParam Long id, Model model) {
        Optional<User> user = userRepository.findById(id);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        model.addAttribute("user", foundUser);
        return "editUserForm";
    }

    //process editing user
    @PostMapping("/editUser")
    public String editUserProcess(@Valid User user, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editUserForm";
        }
        if (userService.saveUser(user, model)) {
            return "redirect:/admin/adminPanel";
        } else {
            return "editUserForm";
        }
    }

    // http://localhost:8080/admin/editUser
    // adding user to database by admin
    @GetMapping("/addUser")
    public String registerUser(Model model) {
        List<Game> gameList = gameRepository.findAll();
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("gameList", gameList);
        return "addUserForm";
    }

    // process adding user
    @PostMapping("/addUser")
    public String validateUsers(@Valid User user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "addUserForm";
        }
        if (userService.saveUser(user, model)) {
            return "redirect:/admin/adminPanel";
        } else {
            return "addUserForm";
        }
    }

    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        userRepository.delete(foundUser);
        return "redirect:/admin/adminPanel";
    }
}