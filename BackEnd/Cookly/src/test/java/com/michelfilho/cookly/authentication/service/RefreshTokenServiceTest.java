package com.michelfilho.cookly.authentication.service;

import com.michelfilho.cookly.authentication.model.RefreshToken;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.authentication.repository.RefreshTokenRepository;
import com.michelfilho.cookly.authentication.repository.UserRepository;
import com.michelfilho.cookly.common.exception.InvalidTokenException;
import com.michelfilho.cookly.common.exception.NotFoundException;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    private final long refreshExpirationMs = 100L;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshExpirationMs", refreshExpirationMs);
        ReflectionTestUtils.setField(refreshTokenService, "secret", "test-secret-key");
    }

    @Test
    public void shouldCreateAValidRefreshToken() {
        User user = Instancio.of(User.class).create();

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        verify(refreshTokenRepository).save(any());
        assertTrue(refreshTokenService.isTokenValid(refreshToken));
    }

    @Test
    public void shouldNotCreateARefreshToken__InvalidUser() {
        assertThrows(NotFoundException.class, () -> {
            refreshTokenService.createRefreshToken(new User("false-id", ""));
        });
    }

    @Test
    public void shouldRefresh() {
        User user = Instancio.of(User.class).create();
        RefreshToken refreshToken = Instancio.of(RefreshToken.class)
                .set(field("expiryDate"), Instant.now().plusSeconds(60))
                .set(field("user"), user)
                .create();

        when(tokenService.generateToken(refreshToken.getUser().getUsername()))
                .thenReturn("valid-token");
        when(refreshTokenRepository.findByToken(refreshToken.getToken()))
                .thenReturn(Optional.of(refreshToken));

        String newToken = refreshTokenService.refresh(refreshToken.getToken());

        assertEquals("valid-token", newToken);
    }

    @Test
    public void shouldNotRefresh__expired() {
        User user = Instancio.of(User.class).create();
        RefreshToken refreshToken = Instancio.of(RefreshToken.class)
                .set(field("expiryDate"), Instant.now().plusSeconds(-60))
                .set(field("user"), user)
                .create();

        when(refreshTokenRepository.findByToken(refreshToken.getToken()))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(InvalidTokenException.class, () -> refreshTokenService.refresh(refreshToken.getToken()));

        verify(refreshTokenRepository).delete(refreshToken);
    }

    @Test
    public void shouldExpire__createdByService() throws InterruptedException {
        User user = Instancio.of(User.class).create();

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        assertTrue(refreshTokenService.isTokenValid(refreshToken));

        Thread.sleep(refreshExpirationMs);

        assertFalse(refreshTokenService.isTokenValid(refreshToken));
    }

    @Test
    public void shouldNotRefresh__expired__createdByService() throws InterruptedException {
        User user = Instancio.of(User.class).create();

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        assertTrue(refreshTokenService.isTokenValid(refreshToken));

        Thread.sleep(refreshExpirationMs);

        assertFalse(refreshTokenService.isTokenValid(refreshToken));

        when(refreshTokenRepository.findByToken(refreshToken.getToken()))
                .thenReturn(Optional.of(refreshToken));

        assertThrows(InvalidTokenException.class, () -> refreshTokenService.refresh(refreshToken.getToken()));

        verify(refreshTokenRepository).delete(refreshToken);
    }

}