package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.common.exception.InvalidPostInteractionStateException;
import com.michelfilho.cookly.common.exception.PostNotFoundException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.model.PostLike;
import com.michelfilho.cookly.post.repository.PostLikeRepository;
import com.michelfilho.cookly.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class InteractPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    public void like(UserDetails user, String postId) {
        if(postLikeRepository.existsByPostIdAndPersonUserUsername(postId, user.getUsername()))
            throw new InvalidPostInteractionStateException("This post is already liked.");

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Person person = personRepository.findByUserUsername(user.getUsername());

        PostLike like = new PostLike(person, post);

        post.getPostLikes().add(like);

        postRepository.save(post);
    }

    public void dislike(UserDetails user, String postId) {
        if(!postLikeRepository.existsByPostIdAndPersonUserUsername(postId, user.getUsername()))
            throw new InvalidPostInteractionStateException("This post isn't liked.");

        postLikeRepository.deleteByPostIdAndPersonUserUsername(postId, user.getUsername());
    }

}
