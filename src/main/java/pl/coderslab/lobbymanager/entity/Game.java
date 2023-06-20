package pl.coderslab.lobbymanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "games")
@Data
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Size(min = 3, max = 30, message = "Name length between 3 and 30.")
    @NotBlank
    private String name;
    @ManyToOne()
    private Rank rank;
    public String getNameWithRank() {
        return name + " - " + rank.getName();
    }
}
