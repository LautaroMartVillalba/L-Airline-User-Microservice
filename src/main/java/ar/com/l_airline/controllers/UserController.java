package ar.com.l_airline.controllers;

import ar.com.l_airline.entities.user.User;
import ar.com.l_airline.dao.UserDAO;
import ar.com.l_airline.dto.UserDTO;
import ar.com.l_airline.exceptionHandler.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.MissingDataException;
import ar.com.l_airline.exceptionHandler.NotFoundException;
import ar.com.l_airline.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Optional<UserDAO>> findByID(@RequestParam Long id){
        Optional<UserDAO> result = service.findUserById(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byEmail")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserDAO>> findByEmailContaining(@RequestParam String email){
        List<UserDAO> result = service.findUserByEmailContaining(email);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byName")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<UserDAO>> findByName(@RequestParam String name){
        List<UserDAO> result = service.fundUserByName(name);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserDAO> insertUser(@RequestBody UserDTO dto) throws ExistingObjectException, MissingDataException {
        UserDAO result = service.createUser(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> deleteUser(@RequestParam Long id) throws NotFoundException {
        boolean result = service.deleteUserById(id);
        if (!result){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/updateInfo")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<User> updateUser(@RequestParam Long id, @RequestBody UserDTO dto){
        User result = service.updateUser(id, dto);

        if (result == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }
}
