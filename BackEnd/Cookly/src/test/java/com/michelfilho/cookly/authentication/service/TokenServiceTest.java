package com.michelfilho.cookly.authentication.service;

import com.michelfilho.cookly.CooklyApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.CoreMatchers.*;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService = new TokenService();

    @BeforeEach
    void setup() {
        tokenService.setSecret("secret");
    }

    @Test
    void generateToken() {
        var token = tokenService.generateToken("michel");
        assertThat(token, is(notNullValue()));
    }

    @Test
    void validateRealToken() {
        var token = tokenService.generateToken("michel");

        var subjectOfToken = tokenService.validateToken(token);

        assertThat(subjectOfToken, is("michel"));
    }

    @Test
    void validateFalseToken() {
        var token = "awesomeToken";

        var subjectOfToken = tokenService.validateToken(token);

        assertThat(subjectOfToken, is(""));
    }
}