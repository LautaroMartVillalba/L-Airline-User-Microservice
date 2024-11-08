package ar.com.l_airline.dao;

import ar.com.l_airline.entities.user.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDAO {
    private Long id;
    private String email;
    private String name;
    @Enumerated(EnumType.STRING)
    private Roles role;
}
