package com.michelfilho.cookly.person.controller;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.person.dto.NewPasswordDTO;
import com.michelfilho.cookly.person.dto.UpdatePersonDTO;
import com.michelfilho.cookly.person.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Return a profile information based on URL."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully returned data."
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Can't find the person based on username."
            )
    })
    @GetMapping("/{username}")
    public ResponseEntity seeProfile(
            @PathVariable @Parameter(example = "johnnyDoe") String username
    ) {
        return ResponseEntity.ok().body(
                personService.getPersonInformation(username)
        );
    }

    @Operation(
            summary = "Return the profile information of the current logged user."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully returned data."
            )
    })
    @GetMapping(value = {"", "/"})
    public ResponseEntity seeLoggedProfile(
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok().body(
                personService.getPersonInformation(user.getUsername())
        );
    }

    @Operation(summary = "Update all user's information, but password")
    @ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Successfully updated"
        )
    })
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

    @Operation(
            summary = "Update user's password",
            description = "After update user password it reset all user's refresh tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated and logged out"
            )
    })
    @PatchMapping("/update/password")
    public ResponseEntity updatePassword(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody NewPasswordDTO dto
    ) {
        personService.updatePassword(
                user.getUsername(),
                dto
        );
        return ResponseEntity.ok().build();
    }

}
