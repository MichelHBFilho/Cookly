package com.michelfilho.cookly.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.michelfilho.cookly.authentication.model.RefreshToken;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.authentication.repository.RefreshTokenRepository;
import com.michelfilho.cookly.authentication.repository.UserRepository;
import com.michelfilho.cookly.common.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshExpirationMs;
    @Value("${api.security.token.secret}")
    private String secret;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private UserRepository userRepository;

    private Instant generateExpirationDate() {
        return Instant.now()
                .plus(refreshExpirationMs, ChronoUnit.MILLIS);
    }

    public RefreshToken createRefreshToken(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class));

        Algorithm algorithm = Algorithm.HMAC256(secret);
        String token = JWT.create()
                .withExpiresAt(generateExpirationDate())
                .withIssuer("Cookly")
                .sign(algorithm);

        RefreshToken refreshToken = new RefreshToken(
                user,
                token,
                generateExpirationDate()
        );
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isTokenValid(RefreshToken refreshToken) {
        return Instant.now().isBefore(refreshToken.getExpiryDate());
    }

}
