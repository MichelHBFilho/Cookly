package com.michelfilho.cookly.person.controller;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.person.dto.CheckFollowingDTO;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.person.service.InteractPersonService;
import com.michelfilho.cookly.person.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
public class InteractPersonController {

    @Autowired
    private InteractPersonService service;
    @Autowired
    private PersonRepository repository;
    @Autowired
    private PersonService personService;

    @Operation(
            summary = "Follow a user based on its username."
    )
    @PostMapping("/follow/{username}")
    public ResponseEntity follow(@PathVariable String username, @AuthenticationPrincipal User user) {
        service.follow(user, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Unfollow a user based on its username."
    )
    @DeleteMapping("/unfollow/{username}")
    public ResponseEntity unfollow(@PathVariable String username, @AuthenticationPrincipal User user) {
        service.unfollow(user, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Returns all followers of a user."
    )
    @GetMapping("/followers/{username}")
    public ResponseEntity getFollowers(@PathVariable String username) {
        return ResponseEntity.ok().body(repository.findByUserUsername(username).getFollowers().stream().map(Person::toReadPersonDTO));
    }

    @Operation(
            summary = "Returns all persons that a user follows."
    )
    @GetMapping("/following/{username}")
    public ResponseEntity getFollowing(@PathVariable String username) {
        return ResponseEntity.ok().body(repository.findByUserUsername(username).getFollowing().stream().map(Person::toReadPersonDTO));
    }

    @Operation(
            summary = "Indicates if the logged user is following a specific user"
    )
    @GetMapping("/follow/{username}")
    public ResponseEntity follows(@PathVariable String username, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(new CheckFollowingDTO(service.isFollowing(user, username)));
    }


}
