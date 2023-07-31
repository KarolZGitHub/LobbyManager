package pl.coderslab.lobbymanager.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.coderslab.lobbymanager.component.OutputMessage;
import pl.coderslab.lobbymanager.entity.Message;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.MessageRepository;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final RoomService roomService;

    public boolean saveMessage(Authentication authentication, @Valid Message message) {
        String content = message.getContent();
        User user = userService.findUserByName(authentication.getName());
        message.setSent(LocalDateTime.now());
        message.setSender(user);
        message.setContent(authentication.getName() + ":" + " " + content);
        messageRepository.save(message);
        return true;
    }
    public void saveMessageFromWebSocket(OutputMessage outputMessage){
        Message message = new Message();
        message.setRoom(roomService.findRoomById(outputMessage.getRoom()));
        message.setContent(outputMessage.getText());
        message.setSent(LocalDateTime.now());
        message.setSender(userService.findUserByName(outputMessage.getSender()));
        messageRepository.save(message);
    }
}
