package pl.coderslab.lobbymanager.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.coderslab.lobbymanager.entity.Game;
import pl.coderslab.lobbymanager.entity.Role;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.GameRepository;
import pl.coderslab.lobbymanager.repository.RoleRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;
import pl.coderslab.lobbymanager.service.MailService;
import pl.coderslab.lobbymanager.service.UserService;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    // http://localhost:8080/admin/adminOnly
    // shows all users, and options

    @GetMapping("/adminPanel")
    public String showAdminPanel(Model model) {
        List<Integer> numberOfRooms = Arrays.asList(50, 100, 150);
        model.addAttribute("numberOfRooms", numberOfRooms);
        List<User> userList = userRepository.findAll().stream().limit(100).toList();
        model.addAttribute("userList", userList);
        return "adminDashboard";
    }

    @PostMapping("/showUserByName")
    public String showUserByName(@RequestParam String userName, Model model) {
        User user = userService.findUserByName(userName);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        model.addAttribute("userList", userList);
        return "foundUser";
    }

    @PostMapping("/showUserById")
    public String showUserByName(@RequestParam Long userId, Model model) {
        User foundUser = userService.findUserById(userId);
        List<User> userList = new ArrayList<>();
        userList.add(foundUser);
        model.addAttribute("userList", userList);
        return "foundUser";
    }

    @PostMapping("/showUserByEmail")
    public String showUserByEmail(@RequestParam String userEmail, Model model) {
        User foundUser = userService.findUserByEmail(userEmail);
        List<User> userList = new ArrayList<>();
        userList.add(foundUser);
        model.addAttribute("userList", userList);
        return "foundUser";
    }

    // http://localhost:8080/admin/editUser
    // editing user in database by admin
    @GetMapping("/editUser")
    public String editUser(@RequestParam Long id, Model model) {
        List<Game> gameList = gameRepository.findAll();
        User foundUser = userService.findUserById(id);
        long roleId = foundUser.getRoles().stream()
                .map(role -> role.getId()).findFirst().get();
        model.addAttribute("roleId", roleId);
        model.addAttribute("user", foundUser);
        model.addAttribute("gameList", gameList);
        return "editUserForm";
    }

    //process editing user
    @PostMapping("/editUser")
    public String editUserProcess(@Valid User user, Model model, BindingResult bindingResult) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (bindingResult.hasErrors()) {
            return "editUserForm";
        }
        userRepository.save(user);
        return "redirect:/admin/adminPanel";
    }

    // http://localhost:8080/admin/editUser
    // adding user to database by admin
    @GetMapping("/addUser")
    public String registerUser(Model model) {
        List<Game> gameList = gameRepository.findAll();
        User user = new User();
        Set<Role> roles = new HashSet<>();
        List<Role> rolesList = roleRepository.findAll();
        for (Role role :
                rolesList) {
            roles.add(role);
        }
        model.addAttribute("user", user);
        model.addAttribute("gameList", gameList);
        model.addAttribute("roles", roles);
        return "addUserForm";
    }

    // process adding user
    @PostMapping("/addUser")
    public String validateUsers(@Valid User user, BindingResult bindingResult, Model model) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (bindingResult.hasErrors()) {
            return "addUserForm";
        }
        if (userService.saveUser(user, model)) {
            return "redirect:/admin/adminPanel";
        } else {
            return "addUserForm";
        }
    }

    // http://localhost:8080/admin/deleteUser
    // deleting user at certain ID from database
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        userRepository.delete(foundUser);
        return "redirect:/admin/adminPanel";
    }

    // http://localhost:8080/admin/banUser
    // sets isActive to false
    @GetMapping("/banUser")
    public String banUser(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        foundUser.setActive(false);
        userRepository.save(foundUser);
        return "redirect:/admin/adminPanel";
    }

    // http://localhost:8080/admin/unbanUser
    // sets isActive to true
    @GetMapping("/unbanUser")
    public String unbanUser(@RequestParam Long id) {
        Optional<User> user = userRepository.findById(id);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        foundUser.setActive(true);
        userRepository.save(foundUser);
        return "redirect:/admin/adminPanel";
    }

    @GetMapping("/sendMail")
    public String sendMail(@RequestParam long id, Model model) {
        User foundUser = userService.findUserById(id);
        model.addAttribute("user", foundUser);
        return "sendMail";
    }

    @PostMapping("/processSendMail")
    public String processSendMail(@RequestParam String subject, @RequestParam String email, @RequestParam String text) {
        try {
            mailService.sendMail(email, subject, text, true);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/admin/adminPanel";
    }
}