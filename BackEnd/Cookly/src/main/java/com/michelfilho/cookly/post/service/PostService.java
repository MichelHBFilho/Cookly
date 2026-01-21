package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.exception.PostNotFoundException;
import com.michelfilho.cookly.common.exception.UnauthorizedException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.model.Recipe;
import com.michelfilho.cookly.post.model.StepToPrepare;
import com.michelfilho.cookly.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PersonRepository personRepository;

    public void publishPost(NewPostDTO data, UserDetails user) {
        Person person = personRepository.findByUserUsername(user.getUsername());

        Recipe newRecipe = new Recipe(
                new ArrayList<StepToPrepare>(),
                data.prepareTime(),
                data.recipeName()
        );

        for (int i = 1; i <= data.stepsToPrepare().size(); i++) {
            String stepToPrepareIterator = data.stepsToPrepare().get(i-1);
            newRecipe.addStep(new StepToPrepare(
                    i,
                    stepToPrepareIterator
            ));
        }

        Post newPost = new Post(
                newRecipe,
                person,
                data.description()
        );

        postRepository.save(newPost);
    }

    public void removePost(String id, UserDetails user) {
        if(!postRepository.existsById(id))
            throw new PostNotFoundException();

        Person person = personRepository.findByUserUsername(user.getUsername());
        if(!postRepository.existsByIdAndPerson(id, person))
            throw new UnauthorizedException("You can't delete this post");

        postRepository.deleteById(id);
    }

    public Page<Post> findPostsByUsername(String username, Integer page) {
        return postRepository.findAllByPersonUserUsernameOrderByCreatedAtDesc(username, PageRequest.of(page, 10));
    }

}
