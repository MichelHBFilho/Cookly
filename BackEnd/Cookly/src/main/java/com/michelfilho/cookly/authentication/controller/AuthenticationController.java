package com.michelfilho.cookly.authentication.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.michelfilho.cookly.authentication.service.AuthenticationService;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.authentication.dto.LoginDTO;
import com.michelfilho.cookly.authentication.dto.LoginResponseDTO;
import com.michelfilho.cookly.authentication.dto.RegisterDTO;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.authentication.repository.UserRepository;
import com.michelfilho.cookly.authentication.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;


    @PostMapping("/login")
    private ResponseEntity login(@RequestBody @Valid LoginDTO data) {
        var token = service.login(data);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        service.register(data);

        return ResponseEntity.ok().build();
    }

}
