package com.michelfilho.cookly.authentication.repository;

import com.michelfilho.cookly.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);
    boolean existsByUsername(String username);

}
