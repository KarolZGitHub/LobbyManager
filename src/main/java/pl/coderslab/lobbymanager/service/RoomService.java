package pl.coderslab.lobbymanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.lobbymanager.entity.Room;
import pl.coderslab.lobbymanager.entity.User;
import pl.coderslab.lobbymanager.repository.RoomRepository;
import pl.coderslab.lobbymanager.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public boolean saveRoom(Room room, Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).orElse(null);
        if (user == null) {
            return false;
        }
        Room foundRoom = roomRepository.findByName(room.getName()).orElse(null);
        if (foundRoom != null) {
            return false;
        }
        room.setCreated(LocalDateTime.now());
        room.setUserList(Arrays.asList(user));
        room.setExpires(LocalDateTime.now().plusDays(2));
        roomRepository.save(room);

        return true;
    }
}
