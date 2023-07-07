package pl.coderslab.lobbymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.lobbymanager.entity.Search;


public interface SearchRepository extends JpaRepository<Search, Long> {

}
