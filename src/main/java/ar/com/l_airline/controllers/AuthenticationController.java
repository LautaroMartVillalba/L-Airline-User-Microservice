package ar.com.l_airline.controllers;

import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.exceptionHandler.custom_exceptions.AccessDeniedException;
import ar.com.l_airline.security.jwt.JwtService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(jwtService.createToken(user.getEmail(), user.getRole()));
    }

    @PostMapping("/validate")
    @SneakyThrows
    public ResponseEntity<String> validateToken(@RequestParam String token) {
        try {
            jwtService.validateToken(token);
        } catch (AccessDeniedException e) {
            throw new AccessDeniedException();
        }
        return ResponseEntity.ok("Nice!");
    }

}
