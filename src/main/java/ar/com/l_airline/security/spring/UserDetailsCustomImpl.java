package ar.com.l_airline.security.spring;

import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.repositories.UserRepository;
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

    /**
     * Search in the database a user and create a UserDetailsCustom object with the default data
     * and the given by the database.
     * @param email the unique data to search one specific user in the database.
     * @return new UserDetailsCustom with the founded user in the database.
     * @throws UsernameNotFoundException if it can't found a user in the database.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);

        return user.map(UserDetailsCustom::new).orElseThrow(()-> new UsernameNotFoundException("User with email '" + email + "' not found."));
    }
}
