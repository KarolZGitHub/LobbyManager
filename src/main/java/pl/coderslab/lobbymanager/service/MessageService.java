package pl.coderslab.lobbymanager.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.lobbymanager.entity.Message;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.MessageRepository;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public boolean saveMessage(Principal principal,@Valid Message message) {
        String content = message.getContent();
        User user = userRepository.findByUserName(principal.getName()).get();
        message.setSent(LocalDateTime.now());
        message.setSender(user);
        message.setContent(principal.getName() + ":" + " " + content);
        messageRepository.save(message);
        return true;
    }
}
