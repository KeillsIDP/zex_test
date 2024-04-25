package me.keills.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private byte age;

    @NonNull
    private String password;

    @ManyToMany(mappedBy = "occupants")
    private List<House> houses;
}
