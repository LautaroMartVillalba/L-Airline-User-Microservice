package ar.com.l_airline.controllers;

import ar.com.l_airline.domains.dto.UserDTO;
import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/byId")
    public ResponseEntity<Optional<UserDTO>> findByID(@RequestParam Long id) {
        return ResponseEntity.ok(service.findUserById(id));
    }

    @GetMapping("/byEmail")
    public ResponseEntity<List<UserDTO>> findByEmailContaining(@RequestParam String email) {
        return ResponseEntity.ok(service.findUserByEmailContaining(email));
    }

    @GetMapping("/byName")
    public ResponseEntity<List<UserDTO>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(service.fundUserByName(name));
    }

    @PostMapping("/insert")
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO dto) throws MessagingException {
        return ResponseEntity.ok(service.createUser(dto));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestParam Long id) {
        service.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/updateInfo")
    public ResponseEntity<User> updateUser(@RequestParam Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.updateUser(id, dto));
    }
}
