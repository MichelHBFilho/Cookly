package com.michelfilho.cookly.authentication.repository;

import com.michelfilho.cookly.authentication.model.RefreshToken;
import com.michelfilho.cookly.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);
    void deleteAllByUser(User user);
}
