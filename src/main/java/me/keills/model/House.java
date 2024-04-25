package me.keills.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "houses")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class House {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NonNull
    @Column(unique = true)
    private String address;

    @NonNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="owner", referencedColumnName = "id")
    private User owner;

    @NonNull
    @ManyToMany(cascade = CascadeType.ALL)
    private List<User> occupants;
}
