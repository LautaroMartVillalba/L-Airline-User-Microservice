package ar.com.l_airline.repositories;

import ar.com.l_airline.domains.entities.TokenRefresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<TokenRefresh, Long> {

    Optional<TokenRefresh> findByEmail(String email);

}
