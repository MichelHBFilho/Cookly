package com.michelfilho.cookly.authentication.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService = new TokenService();

    @BeforeEach
    void setup() {
        tokenService = new TokenService();
        tokenService.setSecret("Secret");
    }

    @Test
    void shouldGenerateValidToken() {
        var token = tokenService.generateToken("michel");
        assertThat(token, is(notNullValue()));
    }

    @Test
    void shouldValidateValidTokenNotExpired() {
        var token = tokenService.generateToken("michel");

        var subjectOfToken = tokenService.validateToken(token);

        assertThat(subjectOfToken, is("michel"));
    }

   /*@Test
    void shouldNotValidateValidTokenExpired() {
        Don't know how to make this test work;
    }*/

    @Test
    void shouldNotValidateFalseToken() {
        var token = "false-token";

        Assertions.assertThrows(Exception.class, () -> {
            tokenService.validateToken(token);
        });
    }
}