package com.michelfilho.cookly.authentication.service;

import com.michelfilho.cookly.CooklyApplication;
import com.michelfilho.cookly.authentication.dto.LoginDTO;
import com.michelfilho.cookly.authentication.dto.RegisterDTO;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.authentication.repository.UserRepository;
import com.michelfilho.cookly.common.exception.UsernameAlreadyRegisteredException;
import com.michelfilho.cookly.person.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(classes = CooklyApplication.class)
@TestPropertySource(properties = {
        "api.security.token.secret=test-secret",
        "api.storage.pictures.profile.path=/src/main/resources/profile_pictures"
})
@Transactional
class AuthenticationServiceTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void shouldLoginWithRealUser() {
        User user = new User(
                "Michel",
                new BCryptPasswordEncoder().encode("password")
        );

        userRepository.save(user);

        var token = authenticationService.login(
                new LoginDTO("Michel", "password")
        );

        assertThat(token, notNullValue());
    }

    @Test
    void shouldNotLoginWithRealUserWrongPassword() {
        User user = new User(
                "Michel",
                new BCryptPasswordEncoder().encode("password")
        );

        userRepository.save(user);

        Assertions.assertThrows(Exception.class, () -> {
            authenticationService.login(
                    new LoginDTO("Michel", "wrongPassword")
            );
        });
    }

    @Test
    void shouldNotLoginWithFalseUser() {
        User user = new User(
                "Michel",
                new BCryptPasswordEncoder().encode("password")
        );

        userRepository.save(user);

        Assertions.assertThrows(Exception.class, () -> {
            authenticationService.login(
                    new LoginDTO("FalseUser", "password")
            );
        });
    }

    @Test
    void shouldRegisterValidUser() {
        RegisterDTO dto = Instancio.of(RegisterDTO.class).create();

        authenticationService.register(dto);

        User user = userRepository.findByUsername(dto.username());

        assertThat(user.getUsername(), is(dto.username()));
    }

    @Test
    void ShouldNotRegisterInvalidUser() {
        RegisterDTO dto = Instancio.of(RegisterDTO.class).create();

        User user = new User(
                dto.username(),
                new BCryptPasswordEncoder().encode("password")
        );

        userRepository.save(user);

        Exception exception = Assertions.assertThrows(UsernameAlreadyRegisteredException.class, () -> {
            authenticationService.register(dto);
        });

    }
}