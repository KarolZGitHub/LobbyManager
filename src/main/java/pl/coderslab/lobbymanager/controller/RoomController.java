package pl.coderslab.lobbymanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.coderslab.lobbymanager.component.OutputMessage;
import pl.coderslab.lobbymanager.entity.Message;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;
import pl.coderslab.lobbymanager.service.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomRepository roomRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final SearchService searchService;
    private final GameService gameService;
    private final RoomService roomService;
    private final UserService userService;

    // http://localhost:8080/room/showRoom
    //shows single room
    @GetMapping("/showRoom")
    public String showRoom(@RequestParam Long id, Model model, Authentication authentication) {
        User user = userRepository.findByUserName(authentication.getName()).get();
        Message message = new Message();
        Room foundRoom = roomService.findRoomById(id);
        String game = foundRoom.getGame().get(0).getName();
        int twitchId = gameService.getGameId(game);
        model.addAttribute("twitchId", twitchId);
        model.addAttribute("room", foundRoom);
        model.addAttribute("message", message);
        model.addAttribute("user", user);
        List<User> userList = foundRoom.getUserList();
        if (userList.stream().anyMatch(u -> u.getId() == (user.getId()))) {
            return "singleRoomUserOnList";
        } else {
            return "singleRoom";
        }
    }

    //Process form searching via game
    @PostMapping("/searchRoom")
    public String showSpecifiedRoomList(@RequestParam String gameWithRank, Authentication authentication, Model model) {
        List<Room> foundRooms = roomRepository.findAll()
                .stream()
                .filter(room -> room.getName().contains(gameWithRank))
                .collect(Collectors.toList());
        if (foundRooms.size() == 0) {
            searchService.saveSearch(gameWithRank, authentication);
        }
        model.addAttribute("foundRooms", foundRooms);
        return "foundRooms";
    }

    // http://localhost:8080/room/addMessage
    // add message to database
    @PostMapping("/addMessage")
    public String addMessage(Message message, @RequestParam Long roomId, Authentication authentication) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room does not exist"));
        message.setRoom(room);
        if (messageService.saveMessage(authentication, message)) {
            return "redirect:/room/showRoom?id=" + room.getId();
        } else {
            return "wrongMessage";
        }
    }

    // http://localhost:8080/room/joinRoom
    //allows user to join room
    @GetMapping("/joinRoom")
    public String joinToRoom(@RequestParam long id, Authentication authentication) {
        Room foundRoom = roomService.findRoomById(id);
        User foundUser = userService.findUserByName(authentication.getName());
        boolean check = true;
        for (User u :
                foundRoom.getUserList()) {
            if (u.getId() == foundUser.getId()) {
                check = false;
            }
        }
        if (check) {
            foundRoom.getUserList().add(foundUser);
        }
        roomRepository.save(foundRoom);
        return "redirect:/users/rooms";
    }

    // http://localhost:8080/room/leaveRoom
    // allows user to leave room
    @GetMapping("/leaveRoom")
    public String leaveRoom(@RequestParam long id, Authentication authentication) {
        Room foundRoom = roomService.findRoomById(id);
        User foundUser = userService.findUserByName(authentication.getName());
        foundRoom.getUserList().removeIf(u -> u.getId() == foundUser.getId());
        roomRepository.save(foundRoom);
        return "redirect:/users/rooms";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(OutputMessage outputMessage) throws Exception {
        messageService.saveMessageFromWebSocket(outputMessage);
        return new OutputMessage(outputMessage.getText(), outputMessage.getSender(), outputMessage.getRoom());
    }
}
