package ar.com.l_airline.domains.dto;

import ar.com.l_airline.domains.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TokenRefreshDTO {
    private Long id;
    private User user;
    private String token;
    private LocalDateTime createDate;
    private String email;
}
