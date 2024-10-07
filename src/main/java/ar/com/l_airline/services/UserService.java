package ar.com.l_airline.services;

import ar.com.l_airline.entities.hotel.Hotel;
import ar.com.l_airline.entities.hotel.HotelDTO;
import ar.com.l_airline.entities.user.User;
import ar.com.l_airline.entities.user.UserDTO;
import ar.com.l_airline.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    private boolean validateUser(UserDTO dto){
        if (dto.getName().isBlank() || dto.getRole().name().isBlank() || dto.getEmail().isBlank() || dto.getPassword().isBlank() || dto.getPassword().length() < 8){
            return false;
        }
        return true;
    }

    public User createUser(UserDTO userDto){
        Optional<User> dbUser = repository.findByEmail(userDto.getEmail());
        if (dbUser.isPresent() && userDto.getPassword().length() < 8){
            return null; //TODO throw exception
        }
        User user = User.builder().email(userDto.getEmail())
                                  .name(userDto.getName())
                                  .password(encoder.encode(userDto.getPassword()))
                                  .role(userDto.getRole())
                                  .isEnabled(userDto.isEnabled())
                                  .accountNoExpired(userDto.isAccountNoExpired())
                                  .accountNoLocked(userDto.isAccountNoLocked())
                                  .credentialsNoExpired(userDto.isCredentialsNoExpired()).build();
        repository.save(user);
        return user;
    }

    public Optional<User> findUserById(Long id){
        if (id == null){
            return Optional.empty();
        }
        return repository.findById(id);
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

    public Optional<User> findUserByEmail(String email){
        if(!email.isBlank() && email.contains("@")){
            return repository.findByEmail(email);
        }
        return Optional.empty();
    }

    public User updateFlight (Long id, UserDTO dto){
        Optional<User> findUser = this.findUserById(id);
        if (!validateUser(dto) || findUser.isEmpty()){
            return null;
        }
        User userFound = findUser.get();

        repository.save(userFound);
        return  userFound;
    }
}
