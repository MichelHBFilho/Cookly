package com.michelfilho.cookly.controller;

import com.michelfilho.cookly.model.user.LoginDTO;
import com.michelfilho.cookly.model.user.LoginResponseDTO;
import com.michelfilho.cookly.model.user.RegisterDTO;
import com.michelfilho.cookly.model.user.User;
import com.michelfilho.cookly.repository.UserRepository;
import com.michelfilho.cookly.services.security.TokenService;
import jakarta.validation.Valid;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    private ResponseEntity login(@RequestBody @Valid LoginDTO data) {
        var usernameAndPassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernameAndPassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if(repository.findByUsername(data.username()) != null)
            return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.username(), encryptedPassword);

        repository.save(newUser);

        return ResponseEntity.ok().build();
    }

}
