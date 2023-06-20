package pl.coderslab.lobbymanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.coderslab.lobbymanager.entity.Game;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.GameRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
        }else {
            return false;
        }
    }
}
