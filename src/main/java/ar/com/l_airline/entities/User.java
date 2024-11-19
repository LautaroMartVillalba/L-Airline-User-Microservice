package ar.com.l_airline.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "entity_user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 70)
    private String email;
    @Column(nullable = false, length = 70)
    private String name;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;

    private boolean isEnabled;
    private boolean accountNoExpired;
    private boolean accountNoLocked;
    private boolean credentialsNoExpired;
}
