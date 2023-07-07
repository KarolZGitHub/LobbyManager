package pl.coderslab.lobbymanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean checkBanned(Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).get();
        if (!user.isActive()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You Are Banned!");
        }
        return true;
    }

    public boolean saveUser(User user, Model model) {
        boolean validation = true;
        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("emailError", "E-mail is taken! Enter different!");
            validation = false;
        }
        if (userRepository.existsByUserName(user.getUserName())) {
            model.addAttribute("usernameError", "User name taken, choose another!");
            validation = false;
        }
        if (validation) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreated(LocalDateTime.now());
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public User findUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        return foundUser;
    }

    public User findUserByName(String name) {
        Optional<User> user = userRepository.findByUserName(name);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        return foundUser;
    }

    public User findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        return foundUser;
    }
}