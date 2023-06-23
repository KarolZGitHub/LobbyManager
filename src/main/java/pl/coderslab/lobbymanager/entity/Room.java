package pl.coderslab.lobbymanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Game> game;

    private LocalDateTime created;

    private LocalDateTime expires;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> userList;

    @OneToMany(mappedBy = "room",fetch = FetchType.EAGER)
    private List<Message> messages;

}