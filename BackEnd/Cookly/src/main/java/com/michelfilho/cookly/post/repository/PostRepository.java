package com.michelfilho.cookly.post.repository;

import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {

    List<Post> findAllByPerson(Person person);

}
