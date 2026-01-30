package com.michelfilho.cookly.person.controller;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.person.dto.UpdatePersonDTO;
import com.michelfilho.cookly.person.service.PersonService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/profile")
public class PersonController {

    @Autowired
    public PersonService personService;

    @GetMapping("/{username}")
    public ResponseEntity seeProfile(@PathVariable String username) {
        return ResponseEntity.ok().body(
                personService.getPersonInformation(username)
        );
    }

    @PatchMapping("/update")
    public ResponseEntity updateProfile(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody UpdatePersonDTO updatePersonDTO
            ) {
        personService.updatePersonInformation(
                updatePersonDTO,
                (User) user
        );
        return ResponseEntity.ok().build();
    }

    /*@PatchMapping("/{username}/password")
    public ResponseEntity updatePassword(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String username
    ) {

        return ResponseEntity.ok().build();
    }*/

}
