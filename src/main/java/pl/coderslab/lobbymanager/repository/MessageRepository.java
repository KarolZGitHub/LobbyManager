package pl.coderslab.lobbymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.lobbymanager.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
}
