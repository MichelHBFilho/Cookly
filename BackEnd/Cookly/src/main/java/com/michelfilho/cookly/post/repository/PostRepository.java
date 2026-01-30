package com.michelfilho.cookly.post.repository;

import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findAllByPerson(Person person);

    @Query(value = """
        SELECT p.*
            FROM post p
            JOIN person pe ON p.person_id = pe.id
            JOIN users u ON pe.user_id = u.id
            WHERE u.username = :username
            ORDER BY p.created_at DESC
            LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Post> findAllByPersonUserUsername(@Param("username") String username, @Param("limit") int limit, @Param("offset") int offset);

    @Query(value = """
        SELECT p.*
            FROM post p
            JOIN person pe ON p.person_id = pe.id
            JOIN users u ON pe.user_id = u.id
            ORDER BY p.created_at DESC
            LIMIT :limit OFFSET :offset
    """, nativeQuery = true)
    List<Post> findAllPaginated(@Param("limit") int limit, @Param("offset") int offset);

    Boolean existsByIdAndPerson(String id, Person person);

}
