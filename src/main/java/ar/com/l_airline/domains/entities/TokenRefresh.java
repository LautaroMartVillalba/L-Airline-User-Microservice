package ar.com.l_airline.domains.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "entity_token")
public class TokenRefresh {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String token;
    @Column(name = "create_at")
    private LocalDateTime createDate;
    @Column(unique = true)
    private String email;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
