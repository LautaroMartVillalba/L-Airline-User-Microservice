package ar.com.l_airline.services;

import ar.com.l_airline.domains.dto.UserDTO;
import ar.com.l_airline.domains.entities.TokenRefresh;
import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.repositories.TokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for managing token-related operations,
 * such as token creation and retrieval by email.
 */
@Service
public class TokenService {

    private TokenRepository repository;
    private PasswordEncoder encoder;
    private UserService userService;


    /**
     * Constructs a new instance of TokenService with required dependencies.
     *
     * @param repository  the token repository used for persistence operations
     * @param encoder     the password encoder used to securely encode tokens
     * @param userService the user service used to manage user-related actions
     */
    public TokenService(TokenRepository repository, PasswordEncoder encoder, UserService userService) {
        this.repository = repository;
        this.encoder = encoder;
        this.userService = userService;
    }

    /**
     * Creates and stores a new token associated with a given user email.
     * The token is encoded for security and linked to the user retrieved by email.
     * Additionally, it updates the user with the new token before saving it to the database.
     *
     * @param token the raw token string to be encoded and stored
     * @param email the email of the user to associate the token with
     */
    public void createToken(String token, String email){

        User user = userService.findUserByEmail(email);

        TokenRefresh dto = TokenRefresh.builder()
                .token(encoder.encode(token))
                .email(email)
                .createDate(LocalDateTime.now())
                .user(user).build();

        UserDTO userDTO = UserDTO.builder().tokens(List.of(dto)).build();
        userService.updateUser(user.getId(), userDTO);

        repository.save(dto);
    }

    /**
     * Retrieves a token associated with a given email address.
     * If the email is empty, a MissingDataException is thrown.
     *
     * @param email the email address to search for an associated token
     * @return an Optional containing the token if found, or empty otherwise
     * @throws MissingDataException if the provided email is empty
     */
    public Optional<TokenRefresh> findByEmail(String email){
        if (email.isEmpty()){
            throw new MissingDataException();
        }

        return repository.findByEmail(email);
    }


}
