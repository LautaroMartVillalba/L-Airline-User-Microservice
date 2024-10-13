package ar.com.l_airline.services;

import ar.com.l_airline.entities.user.User;
import ar.com.l_airline.entities.user.UserDAO;
import ar.com.l_airline.entities.user.UserDTO;
import ar.com.l_airline.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    private boolean validateUser(UserDTO dto){
        return !dto.getName().isBlank() && !dto.getRole().name().isBlank() && !dto.getEmail().isBlank() && !dto.getPassword().isBlank() && dto.getPassword().length() >= 8;
    }

    public UserDAO createUser(UserDTO userDto){
        Optional<User> dbUser = repository.findByEmail(userDto.getEmail());
        if (dbUser.isPresent() || userDto.getPassword().length() < 8 || !validateUser(userDto)){
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

        return UserDAO.builder()
                      .name(userDto.getName())
                      .email(userDto.getEmail())
                      .role(userDto.getRole()).build();
    }

    public Optional<UserDAO> findUserById(Long id){
        if (id == null){
            return Optional.empty();
        }
        User result = repository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));

        return Optional.ofNullable(UserDAO.builder()
                .id(result.getId())
                .name(result.getName())
                .email(result.getEmail())
                .role(result.getRole()).build());
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
    public List<UserDAO> findUserByEmailContaining(String email){
        if (!email.isBlank() && email.contains("@")){
            List<User> result =  repository.findByEmailContaining(email);

            List<UserDAO> daoTransfer = new ArrayList<>();
            result.forEach(user -> daoTransfer.add(UserDAO.builder()
                                                          .id(user.getId())
                                                          .name(user.getName())
                                                          .email(user.getEmail())
                                                          .role(user.getRole()).build()));

            return daoTransfer;
        }
        return new ArrayList<>();
    }

    public List<UserDAO> fundUserByName(String name){
        if (!name.isEmpty() || !name.isBlank()){
            List<User> result = repository.findByNameContaining(name);

            List<UserDAO> daoTransfer = new ArrayList<>();
            result.forEach(user -> daoTransfer.add(UserDAO.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .role(user.getRole()).build()));

            return daoTransfer;
        }
        return new ArrayList<>();
    }

    public Optional<User> findUserByEmail(String email){
        if(!email.isBlank() && email.contains("@")){
            return repository.findByEmail(email);
        }
        return Optional.empty();
    }

    public User updateUser (Long id, UserDTO dto){
        User findUser = repository.findById(id).orElseThrow(()-> new RuntimeException("User not found."));

        if (dto.getName() != null){
            findUser.setName(dto.getName());
        }
        if (dto.getEmail() != null){
            findUser.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null){
            findUser.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null){
            findUser.setRole(dto.getRole());
        }

        repository.save(findUser);
        return findUser;
    }
}
