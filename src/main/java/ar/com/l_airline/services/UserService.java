package ar.com.l_airline.services;

import ar.com.l_airline.entities.user.User;
import ar.com.l_airline.entities.user.UserDTO;
import ar.com.l_airline.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {

    @Autowired
    private UserRepository repository;

    public User createUser(UserDTO userDto){
        Optional<User> dbUser = repository.findByEmail(userDto.getEmail());
        if (dbUser.isEmpty() && userDto.getPassword().length() < 8){
            return null; //TODO throw exception
        }
        User user = User.builder().email(userDto.getEmail())
                                        .name(userDto.getName())
                                        .password(userDto.getPassword()).build();
        repository.save(user);
        return user;
    }

    public boolean deleteUserById(Long id){
        Optional<User> result = repository.findById(id);

        if (result.isEmpty()){
            return false; //TODO throw exception
        }

        repository.deleteById(result.get().getId());
        return true;
    }

    //TODO implement pagination for each GET method
    public List<User> findUserByEmailContaining(String email){
        if (!email.isEmpty() || email.contains("@gmail.com")){
            return repository.findByEmailContaining(email);
        }
        return new ArrayList<>();
    }

    public List<User> fundUserByName(String name){
        if (!name.isEmpty() || !name.isBlank()){
            return repository.findByNameContaining(name);
        }
        return new ArrayList<>();
    }
}
