package pl.coderslab.lobbymanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.coderslab.lobbymanager.entity.Message;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;
import pl.coderslab.lobbymanager.service.MessageService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomController {
    private final RoomRepository roomRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;

    @GetMapping("/showRoom")
    public String showRoom(@RequestParam Long id, Model model, Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).get();
        Message message = new Message();
        Optional<Room> room = roomRepository.findById(id);
        Room foundRoom = room.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room does not exist"));
        model.addAttribute("room", foundRoom);
        model.addAttribute("message", message);
        if (foundRoom.getUserList().contains(user)){
        return "singleRoomUserOnList";
        }else {
            return "singleRoom";
        }
    }

    @PostMapping("/addMessage")
    public String addMessage(Message message, @RequestParam Long roomId, Principal principal) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room does not exist"));
        message.setRoom(room);
        if (messageService.saveMessage(principal, message)) {
            return "redirect:/room/showRoom?id=" + room.getId();
        } else {
            return "wrongMessage";
        }
    }
}
