package ar.com.l_airline.controllers;

import ar.com.l_airline.domains.entities.User;
import ar.com.l_airline.services.JwtService;
import ar.com.l_airline.services.TokenService;
import ar.com.l_airline.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/refresh")
public class TokenRefreshController {

    @Autowired
    private TokenService service;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/recreate")
    public ResponseEntity<String> crear(@RequestParam String token, @RequestParam String email){
        String newToken = jwtService.createToken(email);
        service.createToken(newToken, token, email);
        return ResponseEntity.ok(newToken);
    }

}
