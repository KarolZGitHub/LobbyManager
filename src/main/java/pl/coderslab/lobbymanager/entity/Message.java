package pl.coderslab.lobbymanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String content;

    @ManyToOne
    private User sender;

    @NotNull
    private LocalDateTime sent;

    @ManyToOne
    private Room room;

}
