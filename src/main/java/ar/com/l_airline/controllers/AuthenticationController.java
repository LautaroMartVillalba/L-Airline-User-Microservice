package ar.com.l_airline.controllers;

import ar.com.l_airline.domains.dto.UserDTO;
import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.exceptionHandler.custom_exceptions.*;
import ar.com.l_airline.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @CircuitBreaker(name = "userBreaker", fallbackMethod = "fallback")
    @RateLimiter(name = "jwt")
    @GetMapping("/prueba")
    public ResponseEntity<UserDTO> obtenerDatos(@RequestParam String email, @RequestParam String password){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        if (!authentication.isAuthenticated()) {
            return ResponseEntity.badRequest().build();
        }
        User user = userService.findUserByEmail(email);

        UserDTO dto = UserDTO.builder()
                .role(user.getRole())
                .email(user.getEmail())
                .build();
        return ResponseEntity.ok(dto);
    }

    private ResponseEntity<String> fallback(Exception e){
        if (e instanceof NotFoundException) {
            throw new NotFoundException();
        }
        if (e instanceof AccessDeniedException) {
            throw new AccessDeniedException();
        }
        if (e instanceof ExistingObjectException) {
            throw new ExistingObjectException();
        }
        if (e instanceof MissingDataException) {
            throw new MissingDataException();
        }
        if (e instanceof DebugException) {
            throw new DebugException();
        }

        return new ResponseEntity<>("An error has occurred in our services servers. Please, try again later.", HttpStatusCode.valueOf(503));
    }

}
