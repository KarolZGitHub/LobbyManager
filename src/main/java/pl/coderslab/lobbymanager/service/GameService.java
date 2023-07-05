package pl.coderslab.lobbymanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {
    public int getGameId(String game) {
        int id = 0;
        if(game.contains("League of Legends")){
            id = 21779;
        }else if(game.contains("CS:GO")){
            id = 32399;
        }else if (game.contains("Apex Legends")){
            id = 511224;
        }else if (game.contains("FIFA 23")){
            id = 1745202732;
        }
        return id;
    }
}
