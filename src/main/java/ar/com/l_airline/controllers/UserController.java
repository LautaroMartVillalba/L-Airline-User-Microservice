package ar.com.l_airline.controllers;

import ar.com.l_airline.domains.dto.UserDTO;
import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.exceptionHandler.custom_exceptions.*;
import ar.com.l_airline.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @CircuitBreaker(name = "userBreaker", fallbackMethod = "fallback")
    @RateLimiter(name = "get")
    @GetMapping("/byId")
    public ResponseEntity<UserDTO> findByID(@RequestParam Long id) {
            return ResponseEntity.ok(service.findUserById(id));
    }

    @CircuitBreaker(name = "userBreaker", fallbackMethod = "fallback")
    @RateLimiter(name = "get")
    @GetMapping("/byEmail")
    public ResponseEntity<List<UserDTO>> findByEmailContaining(@RequestParam String email) {
        return ResponseEntity.ok(service.findUserByEmailContaining(email));
    }

    @CircuitBreaker(name = "userBreaker", fallbackMethod = "fallback")
    @RateLimiter(name = "get")
    @GetMapping("/byName")
    public ResponseEntity<List<UserDTO>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(service.fundUserByName(name));
    }

    @CircuitBreaker(name = "userBreaker", fallbackMethod = "fallback")
    @RateLimiter(name = "post-delete-patch")
    @PostMapping("/insert")
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO dto){
        return ResponseEntity.ok(service.createUser(dto));
    }

    @CircuitBreaker(name = "userBreaker", fallbackMethod = "fallback")
    @RateLimiter(name = "post-delete-patch")
    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestParam Long id) {
        service.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @CircuitBreaker(name = "userBreaker", fallbackMethod = "fallback")
    @RateLimiter(name = "post-delete-patch")
    @PatchMapping("/updateInfo")
    public ResponseEntity<UserDTO> updateUser(@RequestParam Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.updateUser(id, dto));
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

        return new ResponseEntity<>("An error has occurred in our services servers. Please, try again later.", HttpStatusCode.valueOf(503));
    }
}
