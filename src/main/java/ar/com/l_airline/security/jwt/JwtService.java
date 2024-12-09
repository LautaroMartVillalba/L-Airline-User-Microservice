package ar.com.l_airline.security.jwt;

import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.domains.enums.Roles;
import ar.com.l_airline.exceptionHandler.custom_exceptions.AccessDeniedException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.services.UserService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    @Autowired
    private UserService service;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Decode the secret value in a byte array, and ser the hashing algorithm with .hmacShaKey().
     */
    public SecretKey getSingKey() {
        byte[] key = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(key);
    }

    /**
     * User getSingKey() to obtain the token's hashing algorithm to and obtain the Payload.
     *
     * @param token User's generated token.
     */
    public void validateToken(String token) {
        if (token == null || token.equals("")){
            throw new AccessDeniedException();
        }
        try {
            Jwts.parser().verifyWith(getSingKey()).build().parseSignedClaims(token).getPayload();
        } catch (JwtException e) {
            throw new AccessDeniedException();
        }
    }

    /**
     * Use the user's email like subject data; role like claim data; set an 15 minutes expiration; and
     * sing the token with the getSingKey() hashing algorithm.
     *
     * @param email User's email.
     * @return Created token with user's info.
     */
    public String createToken(String email) {
        User result = service.findUserByEmail(email);

        if (email.isEmpty()){
            throw new MissingDataException();
        }

        Roles role = result.getRole();

        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role);

        Date at = new Date();
        Date exp = new Date(at.getTime() + 1000 * 60 * 15);

        return Jwts.builder().claims(claims).subject(email).issuedAt(at).expiration(exp).signWith(getSingKey()).compact();
    }

}
