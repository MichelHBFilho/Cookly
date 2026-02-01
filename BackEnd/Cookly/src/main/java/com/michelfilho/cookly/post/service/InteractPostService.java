package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.common.exception.InvalidPostInteractionStateException;
import com.michelfilho.cookly.common.exception.NotFoundException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.repository.CommentRepository;
import com.michelfilho.cookly.post.repository.PostLikeRepository;
import com.michelfilho.cookly.post.repository.PostRepository;
import jakarta.validation.constraints.NotNull;
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
    @Autowired
    private CommentRepository commentRepository;

    public void like(
            UserDetails user,
            String postId
    ) {
        if(postLikeRepository.existsByPostIdAndPersonUserUsername(postId, user.getUsername()))
            throw new InvalidPostInteractionStateException("This post is already liked.");

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class));

        Person person = personRepository.findByUserUsername(user.getUsername());

        post.addLike(person);

        postRepository.save(post);
    }

    public void dislike(
            UserDetails user,
            String postId
    ) {
        if(!postLikeRepository.existsByPostIdAndPersonUserUsername(postId, user.getUsername()))
            throw new InvalidPostInteractionStateException("This post isn't liked.");

        postLikeRepository.deleteByPostIdAndPersonUserUsername(postId, user.getUsername());
    }

    public void comment(
            UserDetails user,
            String postId,
            @NotNull String content
    ) {
        Person person = personRepository.findByUserUsername(user.getUsername());
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(Post.class));

        post.addComment(person, content);

        postRepository.save(post);
    }

    public void removeComment(
            UserDetails user,
            String commentId
    ) {
        if(!commentRepository.existsById(commentId))
            throw new InvalidPostInteractionStateException("This comment does not exist");

        if(!commentRepository.existsByIdAndPersonUserUsername(commentId, user.getUsername()))
            throw new InvalidPostInteractionStateException("This comment is not yours");

        commentRepository.deleteById(commentId);

    }

}
