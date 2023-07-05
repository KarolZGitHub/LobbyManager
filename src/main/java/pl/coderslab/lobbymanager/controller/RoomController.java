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
import pl.coderslab.lobbymanager.entity.Game;
import pl.coderslab.lobbymanager.entity.Message;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;
import pl.coderslab.lobbymanager.service.GameService;
import pl.coderslab.lobbymanager.service.MessageService;
import pl.coderslab.lobbymanager.service.SearchService;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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

    // http://localhost:8080/room/showRoom
    //shows single room
    @GetMapping("/showRoom")
    public String showRoom(@RequestParam Long id, Model model, Authentication authentication) {
        int gameId;
        User user = userRepository.findByUserName(authentication.getName()).get();
        Message message = new Message();
        Optional<Room> room = roomRepository.findById(id);
        Room foundRoom = room.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room does not exist"));
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
        Optional<Room> room = roomRepository.findById(id);
        Room foundRoom = room.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room does not exist"));
        Optional<User> user = userRepository.findByUserName(authentication.getName());
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
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
        Optional<Room> room = roomRepository.findById(id);
        Room foundRoom = room.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room does not exist"));
        Optional<User> user = userRepository.findByUserName(authentication.getName());
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        Iterator<User> iterator = foundRoom.getUserList().iterator();
        while (iterator.hasNext()) {
            User u = iterator.next();
            if (u.getId() == foundUser.getId()) {
                iterator.remove();
            }
        }
        roomRepository.save(foundRoom);
        return "redirect:/users/rooms";
    }
    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        return new OutputMessage(message.getContent());
    }

}
