package com.michelfilho.cookly.post.repository;

import com.michelfilho.cookly.post.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, String> {
    boolean existsByPostIdAndPersonUserUsername(String postId, String username);

    void deleteByPostIdAndPersonUserUsername(String postId, String username);
}
