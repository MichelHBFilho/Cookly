package com.michelfilho.cookly.authentication.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.michelfilho.cookly.authentication.model.RefreshToken;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.authentication.repository.RefreshTokenRepository;
import com.michelfilho.cookly.authentication.repository.UserRepository;
import com.michelfilho.cookly.common.exception.InvalidTokenException;
import com.michelfilho.cookly.common.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    private TokenService tokenService;

    private Instant generateExpirationDate() {
        return Instant.now()
                .plus(refreshExpirationMs, ChronoUnit.MILLIS);
    }

    public String refresh(String requestToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new NotFoundException(RefreshToken.class));

        if(!isTokenValid(refreshToken)) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException();
        }

        return tokenService.generateToken(refreshToken.getUser().getUsername());
    }

    public RefreshToken createRefreshToken(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());

        if(user == null)
            throw new NotFoundException(User.class);

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
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Transactional
    public void logout(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername());
        refreshTokenRepository.deleteAllByUser(user);
    }

    public boolean isTokenValid(RefreshToken refreshToken) {
        return Instant.now().isBefore(refreshToken.getExpiryDate());
    }

}
