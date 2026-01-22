package com.michelfilho.cookly.post.repository;

import com.michelfilho.cookly.post.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {
    boolean existsByIdAndPersonUserUsername(String id, String username);
}
