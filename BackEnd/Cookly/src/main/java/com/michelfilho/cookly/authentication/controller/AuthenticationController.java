package com.michelfilho.cookly.authentication.controller;

import com.michelfilho.cookly.authentication.dto.AccessTokenDTO;
import com.michelfilho.cookly.authentication.dto.LoginDTO;
import com.michelfilho.cookly.authentication.dto.RegisterDTO;
import com.michelfilho.cookly.authentication.service.AuthenticationService;
import com.michelfilho.cookly.authentication.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authentication")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Operation(
            summary = "Authenticate the user with username and password",
            description = "Returns a token for future requests"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully authentication, return a token"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Can't authenticate"
            )
    })
    @PostMapping("/login")
    private ResponseEntity login(
            @RequestBody @Valid LoginDTO data
    ) {
        var token = authenticationService.login(data);
        return ResponseEntity.ok(new AccessTokenDTO(token));
    }

    @Operation(
            summary = "Register a full person",
            description = "Given all person data it register on the database, creating someone able to be logged."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered."),
            @ApiResponse(responseCode = "400", description = "Can't register - Bad Request")
    })
    @PostMapping("/register")
    public ResponseEntity register(
            @ModelAttribute @Valid RegisterDTO data
    ) {
        authenticationService.register(data);

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Return a new JWT token for future requisitions.",
            description = "Given an valid refresh token it returns a new JWT token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "404", description = "Token not found."),
            @ApiResponse(responseCode = "400", description = "Token expired.")
    })
    @GetMapping("/refresh/{token}")
    public ResponseEntity refresh(
        @PathVariable @Parameter(example = "BEARER TOKEN") String token
    ) {
        return ResponseEntity.ok().body(new AccessTokenDTO(refreshTokenService.refresh(token)));
    }

    @Operation(
            summary = "Return a new refresh token.",
            description = "When authenticated, it returns a valid refresh token, that will last for a long time."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success."),
            @ApiResponse(responseCode = "403", description = "User isn't logged.")
    })
    @GetMapping("/generate-refresh")
    public ResponseEntity generateRefresh(
            @AuthenticationPrincipal UserDetails userDetails
            ) {
        return ResponseEntity.ok().body(refreshTokenService.createRefreshToken(userDetails));
    }

    @Operation(
            summary = "Delete all refresh tokens."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.")
    })
    @DeleteMapping("/logout")
    public ResponseEntity logout(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        refreshTokenService.logout(userDetails);
        return ResponseEntity.ok().build();
    }

}
