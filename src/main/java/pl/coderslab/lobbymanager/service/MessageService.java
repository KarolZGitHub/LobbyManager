package pl.coderslab.lobbymanager.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.coderslab.lobbymanager.entity.Message;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.MessageRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public boolean saveMessage(Authentication authentication, @Valid Message message) {
        String content = message.getContent();
        User user = userRepository.findByUserName(authentication.getName()).get();
        message.setSent(LocalDateTime.now());
        message.setSender(user);
        message.setContent(authentication.getName() + ":" + " " + content);
        messageRepository.save(message);
        return true;
    }
}
