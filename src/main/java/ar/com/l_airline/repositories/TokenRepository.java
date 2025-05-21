package ar.com.l_airline.repositories;

import ar.com.l_airline.domains.entities.TokenRefresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenRefresh, Long> {

    List<TokenRefresh> findByEmail(String email);
    Optional<TokenRefresh> findByToken(String token);
    Optional<TokenRefresh> findByEmailAndOriginalTokenTrue(String email);
    Optional<TokenRefresh> findByEmailAndAvailableFalseAndRevokedTrueAndOriginalTokenFalse(String email);

}
