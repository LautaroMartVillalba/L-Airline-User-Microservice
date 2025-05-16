package ar.com.l_airline.domains.dto;

import ar.com.l_airline.domains.entities.TokenRefresh;
import ar.com.l_airline.domains.enums.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String name;
    private String password;
    private List<TokenRefresh> tokens;

    @Enumerated(EnumType.STRING)
    private Roles role;
    private boolean isEnabled;
    private boolean accountNoExpired;
    private boolean accountNoLocked;
    private boolean credentialsNoExpired;
}
