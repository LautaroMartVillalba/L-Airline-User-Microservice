package ar.com.l_airline.services;

import ar.com.l_airline.domains.dto.TokenRefreshDTO;
import ar.com.l_airline.domains.dto.UserDTO;
import ar.com.l_airline.domains.entities.TokenRefresh;
import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.repositories.TokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    private TokenRepository repository;
    private PasswordEncoder encoder;
    private UserService userService;


    public TokenService(TokenRepository repository, PasswordEncoder encoder, UserService userService) {
        this.repository = repository;
        this.encoder = encoder;
        this.userService = userService;
    }

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

    public Optional<TokenRefresh> findByEmail(String email){
        if (email.isEmpty()){
            throw new MissingDataException();
        }

        return repository.findByEmail(email);
    }


}
