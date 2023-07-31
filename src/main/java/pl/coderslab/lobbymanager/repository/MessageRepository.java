package pl.coderslab.lobbymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.lobbymanager.entity.Message;
import pl.coderslab.lobbymanager.entity.User;

import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Optional<Message> findBySender(User user);
}
