package pl.coderslab.lobbymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.lobbymanager.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}