package com.michelfilho.cookly.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.exception.InvalidTokenException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
@NoArgsConstructor
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private Clock clock = Clock.systemDefaultZone();


    //Just for tests
    protected void setClock(Clock clock) {
        this.clock = clock;
    }

    protected void setSecret(String secret) {
        this.secret = secret;
    }


    public String generateToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("Cookly")
                    .withSubject(username)
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);
            return token;
        } catch(JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("Cookly")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch(JWTVerificationException exception) {
            throw new InvalidTokenException();
        }
    }

    private Instant generateExpirationDate() {
        return Instant.now(clock)
                .plus(2, ChronoUnit.HOURS);
    }
}
