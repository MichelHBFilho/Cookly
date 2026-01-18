package com.michelfilho.cookly.authentication.service;

import com.michelfilho.cookly.authentication.dto.LoginDTO;
import com.michelfilho.cookly.authentication.dto.RegisterDTO;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.authentication.repository.UserRepository;
import com.michelfilho.cookly.common.exception.UsernameAlreadyRegisteredException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private TokenService tokenService;

    public String login(LoginDTO data) {
        var usernameAndPassword = new UsernamePasswordAuthenticationToken(
                data.username(),
                data.password()
        );

        var auth = this.authenticationManager.authenticate(usernameAndPassword);

        var token = tokenService.generateToken(auth.getName());

        return token;
    }

    public void register(RegisterDTO data) {
        if(userRepository.existsByUsername(data.username()))
            throw new UsernameAlreadyRegisteredException(data.username());

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.username(), encryptedPassword);
        Person newPerson = new Person(
                data.name(),
                data.surName(),
                data.profilePicturePath(),
                data.birthDay(),
                newUser
        );

        personRepository.save(newPerson);
    }

}
