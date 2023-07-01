package pl.coderslab.lobbymanager.controller;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import pl.coderslab.lobbymanager.entity.*;
import pl.coderslab.lobbymanager.repository.GameRepository;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.repository.SearchRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;
import pl.coderslab.lobbymanager.service.RoomService;
import pl.coderslab.lobbymanager.service.SearchService;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final RoomService roomService;
    private final GameRepository gameRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SearchService searchService;
    private final SearchRepository searchRepository;

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

    // adding room to database,
    // clears old searches from database
    // and sends e-mail if some user is looking for room with featuring game
    @PostMapping("/addRoom")
    public String processCreateRoomForm(Authentication authentication, Room room, BindingResult bindingResult, Model model) throws MessagingException {
        List<Search> searches = searchRepository.findAll();
        if (bindingResult.hasErrors()) {
            return "createRoomForm";
        }
        room.setName(authentication.getName() + " " + room.getGame().get(0).getNameWithRank());
        if (roomService.saveRoom(room, authentication)) {
            List<Game> gameList = gameRepository.findAll();
            model.addAttribute("gameList", gameList);
            searchService.cleanSearches(searches);
            searchService.sendMailIfFoundRoom(searches,room);
            return "redirect:/users/rooms";
        } else {
            return "roomExists";
        }
    }

    // http://localhost:8080/users/rooms
    // shows all rooms and its actions
    @PostMapping("/rooms")
    public String showRooms(@RequestParam int number, Model model) {
        List<Game> gameList = gameRepository.findAll();
        List<Room> rooms = roomRepository.findAll();
        List<Room> limitedRooms = rooms.stream().limit(number)
                .collect(Collectors.toList());
        model.addAttribute("rooms", limitedRooms);
        model.addAttribute("gameList", gameList);
        roomService.cleanRooms(rooms);
        return "roomsForUser";
    }

    // After adding new room shows latest 200 rooms.
    @GetMapping("/rooms")
    public String showRoomsAfterAdding(Model model) {
        List<Game> gameList = gameRepository.findAll();
        int number = 200;
        List<Room> rooms = roomRepository.findAll();
        List<Room> limitedRooms = rooms.stream().limit(number)
                .collect(Collectors.toList());
        model.addAttribute("rooms", limitedRooms);
        model.addAttribute("gameList", gameList);
        roomService.cleanRooms(rooms);
        return "roomsForUser";
    }
    // http://localhost:8080/users/home
    // shows main page for user

    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        Optional<User> user = userRepository.findByUserName(principal.getName());
        List<Integer> numberOfRooms = Arrays.asList(50, 100, 150);
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        List<String> role = foundUser.getRoles().stream().map(Role::getName)
                .toList();
        model.addAttribute("numberOfRooms", numberOfRooms);
        model.addAttribute("user", foundUser);
        model.addAttribute("role", role.get(0));
        roomService.cleanRooms(roomRepository.findAll());
        return "homeForUser";
    }

    // http://localhost:8080/users/changePassword
    //Show change password form.
    @GetMapping("/changePassword")
    public String showChangePasswordForm(Principal principal, Model model) {
        Optional<User> user = userRepository.findByUserName(principal.getName());
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        model.addAttribute("user", foundUser);
        return "changePassword";
    }

    //Process change password.
    @PostMapping("/changePassword")
    public String processChangePassword(@RequestParam String password, Principal principal) {
        User foundUser = userRepository.findByUserName(principal.getName()).get();
        foundUser.setPassword(passwordEncoder.encode(password));
        userRepository.save(foundUser);
        return "redirect:/users/home";
    }

    // http://localhost:8080/users/changeEmail
    //Show change E-mail form.
    @GetMapping("/changeEmail")
    public String showChangeEmailForm(Model model, Principal principal) {
        Optional<User> user = userRepository.findByUserName(principal.getName());
        User foundUser = user.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist"));
        model.addAttribute("user", foundUser);
        return "changeEmail";
    }

    //Process change E-mail.
    @PostMapping("/changeEmail")
    public String processChangeEmail(@RequestParam String email, Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).get();
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/users/home";
    }
}