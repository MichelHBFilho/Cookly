package com.michelfilho.cookly.repository;

import com.michelfilho.cookly.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

}
