package com.michelfilho.cookly.person.controller;

import com.michelfilho.cookly.person.service.PersonService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
