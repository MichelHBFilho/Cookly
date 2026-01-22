package com.michelfilho.cookly.authentication.service;

import com.michelfilho.cookly.CooklyApplication;
import com.michelfilho.cookly.authentication.dto.LoginDTO;
import com.michelfilho.cookly.authentication.dto.RegisterDTO;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.authentication.repository.UserRepository;
import com.michelfilho.cookly.common.exception.UsernameAlreadyRegisteredException;
import com.michelfilho.cookly.person.repository.PersonRepository;
import jakarta.transaction.Transactional;
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
        "api.security.token.secret=test-secret"
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
        RegisterDTO dto = new RegisterDTO(
                "mhbFilho",
                "password",
                "Michel",
                "Filho",
                "",
                LocalDate.of(2007, 10, 2)
        );

        authenticationService.register(dto);

        User user = userRepository.findByUsername("mhbFilho");

        assertThat(user.getUsername(), is("mhbFilho"));
    }

    @Test
    void ShouldNotRegisterInvalidUser() {
        User user = new User(
                "mhbFilho",
                new BCryptPasswordEncoder().encode("password")
        );

        userRepository.save(user);

        RegisterDTO dto = new RegisterDTO(
                "mhbFilho",
                "password",
                "Michel",
                "Filho",
                "",
                LocalDate.of(2007, 10, 2)
        );

        Exception exception = Assertions.assertThrows(UsernameAlreadyRegisteredException.class, () -> {
            authenticationService.register(dto);
        });

        assertThat(exception.getMessage(), is("The username mhbFilho was already taken."));

    }
}