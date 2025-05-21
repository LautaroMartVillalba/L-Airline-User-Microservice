package ar.com.l_airline.services;

import ar.com.l_airline.domains.entities.TokenRefresh;
import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.exceptionHandler.custom_exceptions.DebugException;
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

    private final TokenRepository repository;
    private final PasswordEncoder encoder;
    private final UserService userService;


    /**
     * Constructs a new instance of TokenService with required dependencies.
     *
     * @param repository  the token repository used for persistence operations
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
     * @param email the email of the user to associate the token with
     */
    public void createToken(String newToken, String oldToken, String email){
//        if (!this.validToken(token, email)){
//            throw new DebugException();
//        }

        User user = userService.findUserByEmail(email);

        TokenRefresh tokenToDB = TokenRefresh.builder()
                .token(newToken)
                .email(email)
                .createDate(LocalDateTime.now())
                .user(user)
                .available(true)
                .revoked(false)
                .originalToken(true)
                .build();

        Optional<TokenRefresh> originalToken = repository.findByEmailAndOriginalTokenTrue(email);
        originalToken.ifPresent(tokenRefresh -> {
            tokenRefresh.setAvailable(false);
            tokenRefresh.setRevoked(true);
            repository.save(tokenRefresh);
            tokenToDB.setOriginalToken(false);
        });

        if(oldToken != null) {
            Optional<TokenRefresh> actualToken = repository.findByToken(oldToken);
            actualToken.ifPresent(actual -> {
                Optional<TokenRefresh> toDeleteToken = repository.findByEmailAndAvailableFalseAndRevokedTrueAndOriginalTokenFalse(email);
                toDeleteToken.ifPresent(repository::delete);
                actual.setAvailable(false);
                actual.setRevoked(true);
                repository.save(actual);
            });
        }

        repository.save(tokenToDB);
    }

    /**
     * Retrieves a token associated with a given email address.
     * If the email is empty, a MissingDataException is thrown.
     *
     * @param email the email address to search for an associated token
     * @return an Optional containing the token if found, or empty otherwise
     * @throws MissingDataException if the provided email is empty
     */
    public List<TokenRefresh> findByEmail(String email){
        if (email.isEmpty()){
            throw new MissingDataException();
        }

        return repository.findByEmail(email);
    }

    public boolean validToken(String token, String email){
        Optional<TokenRefresh> tokenInDB = repository.findByToken(token);
        Optional<TokenRefresh> originalToken = repository.findByEmailAndOriginalTokenTrue(email);

        if (tokenInDB.isPresent() && !tokenInDB.get().isAvailable() || tokenInDB.isPresent() && tokenInDB.get().isRevoked()){
            return false;
        }

        if (originalToken.isPresent()){
            if (!originalToken.get().getCreateDate().plusMinutes(20).isBefore(LocalDateTime.now())){
                return false;
            }
        }
        return true;
    }


}
