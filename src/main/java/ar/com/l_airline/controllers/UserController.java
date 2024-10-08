package ar.com.l_airline.controllers;

import ar.com.l_airline.entities.user.User;
import ar.com.l_airline.entities.user.UserDTO;
import ar.com.l_airline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping("/byId/{id}")
    public ResponseEntity<Optional<User>> findByID(@PathVariable Long id){
        Optional<User> result = service.findUserById(id);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<List<User>> findByEmailContaining(@PathVariable String email){
        List<User> result = service.findUserByEmailContaining(email);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity<List<User>> findByname(@PathVariable String name){
        List<User> result = service.fundUserByName(name);
        if (result.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/insert")
    public ResponseEntity<User> insertUser(@RequestBody UserDTO dto){
        service.createUser(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Long id){
        boolean result = service.deleteUserById(id);
        if (!result){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/updateInfo/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserDTO dto){
        User result = service.updateUser(id, dto);

        if (result == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }
}
