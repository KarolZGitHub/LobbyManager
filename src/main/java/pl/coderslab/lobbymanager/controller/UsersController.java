package pl.coderslab.lobbymanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.lobbymanager.entity.Game;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.repository.GameRepository;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.service.RoomService;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final RoomService roomService;
    private final GameRepository gameRepository;
    private final RoomRepository roomRepository;

    // http://localhost:8080/users/addRoom
    // adding room to database
    @GetMapping("/addRoom")
    public String showCreateRoomForm(Model model) {
        List<Game> gameList = gameRepository.findAll();
        Room room = new Room();
        model.addAttribute("room", room);
        model.addAttribute("gameList", gameList);
        return "createRoomForm";
    }

    // adding room to database
    @PostMapping("/addRoom")
    public String processCreateRoomForm(Principal principal, Room room, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createRoomForm";
        }
        room.setName(principal.getName() + " " + room.getGame().get(0).getNameWithRank());
        if (roomService.saveRoom(room, principal, model)) {
            return "redirect:/users/home";
        } else {
            return "roomExists";
        }
    }

    // http://localhost:8080/users/home
    // shows main page for user
    // shows all rooms and its actions
    @GetMapping("/home")
    public String showHome(Model model) {
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "homeForUser";
    }
}