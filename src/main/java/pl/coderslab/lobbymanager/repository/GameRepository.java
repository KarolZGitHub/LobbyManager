package pl.coderslab.lobbymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.lobbymanager.entity.Game;

public interface GameRepository extends JpaRepository<Game,Long> {
}
