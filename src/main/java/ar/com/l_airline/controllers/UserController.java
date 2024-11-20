package ar.com.l_airline.controllers;

import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.domains.dto.UserDTO;
import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import ar.com.l_airline.services.UserService;
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

    @GetMapping("/byId/{id}")
    public ResponseEntity<Optional<UserDTO>> findByID(@PathVariable Long id){
        Optional<UserDTO> result = service.findUserById(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byEmail")
    public ResponseEntity<List<UserDTO>> findByEmailContaining(@RequestParam String email){
        List<UserDTO> result = service.findUserByEmailContaining(email);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byName")
    public ResponseEntity<List<UserDTO>> findByName(@RequestParam String name){
        List<UserDTO> result = service.fundUserByName(name);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/insert")
    public ResponseEntity<UserDTO> insertUser(@RequestBody UserDTO dto) throws ExistingObjectException, MissingDataException {
        UserDTO result = service.createUser(dto);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<User> deleteUser(@RequestParam Long id) throws NotFoundException {
        boolean result = service.deleteUserById(id);
        if (!result){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/updateInfo")
    public ResponseEntity<User> updateUser(@RequestParam Long id, @RequestBody UserDTO dto){
        User result = service.updateUser(id, dto);

        if (result == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }
}
