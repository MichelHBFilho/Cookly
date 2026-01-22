package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.exception.InvalidPostInteractionStateException;
import com.michelfilho.cookly.common.exception.PostNotFoundException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.model.PostLike;
import com.michelfilho.cookly.post.repository.PostLikeRepository;
import com.michelfilho.cookly.post.repository.PostRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import static org.instancio.Select.field;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InteractPostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PostLikeRepository postLikeRepository;

    @InjectMocks
    private InteractPostService service;

    @Test
    public void shouldLikeThePost() {
        UserDetails user = new User();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        Post post = Instancio.of(Post.class)
                .set(field("person"), person)
                .create();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(personRepository.findByUserUsername(user.getUsername())).thenReturn(person);

        service.like(user, post.getId());

        verify(postRepository).save(post);
    }

    @Test
    public void shouldNotLikeAlreadyLikedPost() {
        UserDetails user = new User();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        Post post = Instancio.of(Post.class)
                .set(field("person"), person)
                .create();

        post.getPostLikes().add(new PostLike(person, post));

        when(postLikeRepository.existsByPostIdAndPersonUserUsername(post.getId(), user.getUsername())).thenReturn(true);

        assertThrows(InvalidPostInteractionStateException.class, () -> {
            service.like(user, post.getId());
        });
    }

    @Test
    public void shouldNotLikeUnexistentPost() {
        UserDetails user = new User();

        when(postRepository.findById("false-id"))
                .thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> {
            service.like(user, "false-id");
        });
    }

    @Test
    public void shouldDislikeThePost() {
        UserDetails user = Instancio.of(User.class).create();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        Post post = Instancio.of(Post.class)
                .set(field("person"), person)
                .create();

        when(postLikeRepository.existsByPostIdAndPersonUserUsername(post.getId(), user.getUsername())).thenReturn(true);

        service.dislike(user, post.getId());

        verify(postLikeRepository).deleteByPostIdAndPersonUserUsername(post.getId(), user.getUsername());
    }

    @Test
    public void shouldNotDislikeThePost_NotLiked() {
        UserDetails user = new User();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        Post post = Instancio.of(Post.class)
                .set(field("person"), person)
                .create();

        when(postLikeRepository.existsByPostIdAndPersonUserUsername(post.getId(), user.getUsername())).thenReturn(false);

        assertThrows(InvalidPostInteractionStateException.class, () -> service.dislike(user, post.getId()));
    }

}