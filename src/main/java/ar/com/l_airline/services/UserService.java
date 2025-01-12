package ar.com.l_airline.services;

import ar.com.l_airline.domains.dto.UserDTO;
import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.repositories.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private static UserRepository repository;
    private static PasswordEncoder encoder;
    private static MailService mailService;

    public UserService(UserRepository repository, PasswordEncoder encoder, MailService mailService) {
        UserService.repository = repository;
        UserService.encoder = encoder;
        UserService.mailService = mailService;
    }

    /**
     * Check if any data is empty.
     *
     * @param dto User data to check.
     * @return False if any data are blank or empty. True if not.
     */
    private boolean validateUser(UserDTO dto) {
        return !dto.getName().isBlank()
                && !dto.getRole().name().isBlank()
                && !dto.getEmail().isBlank()
                && !dto.getPassword().isBlank();
    }

    /**
     * Persist a User in the DataBase.
     *
     * @param userDto User data to persist.
     * @return User created information (without the encoded password).
     */
    public UserDTO createUser(UserDTO userDto){
        if (!validateUser(userDto)) {
            throw new MissingDataException();
        }

        Optional<User> dbUser = repository.findByEmail(userDto.getEmail());

        if (dbUser.isPresent()) {
            throw new ExistingObjectException();
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

        //Send a "thanks" email
        try {
            mailService.sendMail(user.getEmail(), "Thanks for register!", user.getName());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return UserDTO.builder()
                .id(user.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .role(userDto.getRole()).build();
    }

    /**
     * Search one record in the DataBase with id number matching.
     *
     * @param id Identification number.
     * @return User info if exist any matching in the DataBase. Empty optional if id number is null.
     * @throws RuntimeException if it can't found one matching in the DataBase.
     */
    public Optional<UserDTO> findUserById(Long id) {
        if (id == null) {
            throw new MissingDataException();
        }
        User result = repository.findById(id).orElseThrow(NotFoundException::new);

        return Optional.ofNullable(UserDTO.builder()
                .id(result.getId())
                .name(result.getName())
                .email(result.getEmail())
                .role(result.getRole()).build());
    }

    /**
     * Search and delete (if exists one matcher) one record in the DataBase.
     *
     * @param id Identification number.
     * @return False if it can't found one record in the DataBase. True if it can found and delete.
     */
    public boolean deleteUserById(Long id) {
        if (id == null) {
            throw new MissingDataException();
        }
        User result = repository.findById(id).orElseThrow(NotFoundException::new);

        repository.deleteById(result.getId());
        return true;
    }

    /**
     * Search some given email matchers in the DataBase, mapping the records to a User Data Access Object to protect the password.
     *
     * @param email Users email.
     * @return List of User Data Access Object if it can found some records in the DataBase. Empty list if not, or email is blank or invalid.
     */
    public List<UserDTO> findUserByEmailContaining(String email) {
        if (email.isBlank() && !email.contains("@")) {
            throw new MissingDataException();
        }
        List<User> result = repository.findByEmailContaining(email);
        if (result.isEmpty()) {
            throw new NotFoundException();
        }

        List<UserDTO> dtoTransfer = new ArrayList<>();
        result.forEach(user -> dtoTransfer.add(UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole()).build()));

        return dtoTransfer;
    }

    /**
     * Search some records in the DataBase that matching with the given name.
     *
     * @param name User name.
     * @return List of User Data Access Object if it can found some records in the DataBase. Empty list if not.
     */
    public List<UserDTO> fundUserByName(String name) {
        if (name.isEmpty()){
            throw new MissingDataException();
        }

        List<User> result = repository.findByNameContaining(name);

        if (result.isEmpty()) {
            throw new NotFoundException();
        }

        List<UserDTO> daoTransfer = new ArrayList<>();
        result.forEach(user -> daoTransfer.add(UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole()).build()));

        return daoTransfer;
    }

    /**
     * Search one User in the DataBase by his email.
     *
     * @param email User email.
     * @return Optional of User Data Access Object if it can found one record in the DataBase. Empty optional if not.
     */
    public User findUserByEmail(String email) {
        if (email.isBlank() || !email.contains("@")) {
            throw new MissingDataException();
        }

        User result = repository.findByEmail(email).orElseThrow(NotFoundException::new);

        return User.builder()
                .id(result.getId())
                .name(result.getName())
                .email(result.getEmail())
                .role(result.getRole()).build();
    }

    /**
     * Replace one or more data of an existing user in the DataBase.
     *
     * @param id  Identification Number
     * @param dto User data to change and persist.
     * @return User Data Access Object with the changes.
     */
    public User updateUser(Long id, UserDTO dto) {
        User findUser = repository.findById(id).orElseThrow(NotFoundException::new);

        if (dto.getName() != null) {
            findUser.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            findUser.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null) {
            findUser.setPassword(dto.getPassword());
        }
        if (dto.getRole() != null) {
            findUser.setRole(dto.getRole());
        }

        repository.save(findUser);
        return User.builder()
                .id(findUser.getId())
                .name(findUser.getName())
                .email(findUser.getEmail())
                .role(findUser.getRole()).build();
    }
}