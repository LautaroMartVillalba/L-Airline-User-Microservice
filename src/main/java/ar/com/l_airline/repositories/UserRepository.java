package ar.com.l_airline.repositories;

import ar.com.l_airline.domains.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmailContaining(String email);

    Optional<User> findByEmail(String email);

    List<User> findByNameContaining(String name);
}
