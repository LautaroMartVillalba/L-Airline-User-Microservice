package ar.com.l_airline.services;

import ar.com.l_airline.entities.Roles;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public SecretKey getSingKey() {
        byte[] key = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(key);
    }

    public void validateToken(String token) {
        Jwts.parser().verifyWith(getSingKey()).build().parseSignedClaims(token).getPayload();
    }

    public String createToken(String email, Roles role) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role);

        Date at = new Date();
        Date exp = new Date(at.getTime() + 1000*60*15);

        return Jwts.builder().claims(claims).subject(email).issuedAt(at).expiration(exp).signWith(getSingKey()).compact();
    }
}
