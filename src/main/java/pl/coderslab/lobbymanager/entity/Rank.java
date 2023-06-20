package pl.coderslab.lobbymanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "ranks")
@Data
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Size(min = 3, max = 20, message = "Name length between 3 and 20.")
    private String name;

}
