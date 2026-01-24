package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.exception.InvalidPostInteractionStateException;
import com.michelfilho.cookly.common.exception.NotFound;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.model.Comment;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.model.PostLike;
import com.michelfilho.cookly.post.repository.CommentRepository;
import com.michelfilho.cookly.post.repository.PostLikeRepository;
import com.michelfilho.cookly.post.repository.PostRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
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

    @Mock
    private CommentRepository commentRepository;

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

        assertThrows(NotFound.class, () -> {
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

    @Test
    public void shouldCommentThePost() {
        UserDetails user = new User();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        Post post = Instancio.of(Post.class)
                .set(field("person"), person)
                .create();

        String content = Instancio.of(String.class).create();

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(personRepository.findByUserUsername(user.getUsername())).thenReturn(person);

        service.comment(user, post.getId(), content);

        verify(postRepository).save(post);
    }

    @Test
    public void shouldNotCommentUnexistentPost() {
        UserDetails user = new User();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        String postId = "false-id";

        String content = Instancio.of(String.class).create();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());
        when(personRepository.findByUserUsername(user.getUsername())).thenReturn(person);


        Assertions.assertThrows(NotFound.class, () -> {
            service.comment(user, postId, content);
        });
    }

    @Test
    public void shouldRemoveComment() {
        UserDetails user = Instancio.of(User.class).create();

        Comment comment = Instancio.of(Comment.class).create();

        when(commentRepository.existsByIdAndPersonUserUsername(comment.getId(), user.getUsername())).thenReturn(true);
        when(commentRepository.existsById(comment.getId())).thenReturn(true);

        service.removeComment(user, comment.getId());

        verify(commentRepository).deleteById(comment.getId());
    }

    @Test
    public void shouldNotRemoveUnexistentComment() {
        UserDetails user = Instancio.of(User.class).create();

        String commentId = "false-id";

        Assertions.assertThrows(InvalidPostInteractionStateException.class, () -> {
            service.removeComment(user, commentId);
        });
    }

    @Test
    public void shouldNotRemoveNotMyComment() {
        UserDetails user = Instancio.of(User.class).create();

        Comment comment = Instancio.of(Comment.class).create();

        when(commentRepository.existsById(comment.getId())).thenReturn(true);
        when(commentRepository.existsByIdAndPersonUserUsername(comment.getId(), user.getUsername())).thenReturn(false);

        Assertions.assertThrows(InvalidPostInteractionStateException.class, () -> {
            service.removeComment(user, comment.getId());
        });
    }

}