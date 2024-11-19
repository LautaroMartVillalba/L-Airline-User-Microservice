package ar.com.l_airline.security;

import ar.com.l_airline.entities.User;
import ar.com.l_airline.repositories.UserRepository;
import ar.com.l_airline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsCustomImpl implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);

        return user.map(UserDetailsCustom::new).orElseThrow(()-> new UsernameNotFoundException("User with email '" + email + "' not found."));
    }
}
