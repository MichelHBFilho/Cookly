package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.exception.NotFoundException;
import com.michelfilho.cookly.common.exception.UnauthorizedException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.dto.ReadPostDTO;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.repository.PostRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("Test")
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private PostService postService;

    @Test
    public void shouldPublishValidPost() {
        NewPostDTO post = Instancio.of(NewPostDTO.class).create();
        UserDetails user = Instancio.of(User.class).create();
        Person person = Instancio.of(Person.class).create();

        postService.publishPost(post, null, user);

        verify(postRepository).save(any());
    }

    @Test
    public void shouldRemoveValidPost() {
        String postId = "id";
        User user = Instancio.of(User.class).create();
        Person person = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(user.getUsername())).thenReturn(person);
        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.existsByIdAndPerson(postId, person)).thenReturn(true);

        postService.removePost(postId, user);

        verify(postRepository).deleteById(postId);
    }

    @Test
    public void shouldNotRemoveInexistentPost() {
        String postId = "id";
        User user = Instancio.of(User.class).create();

        when(postRepository.existsById(postId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            postService.removePost(postId, user);
        });
    }

    @Test
    public void shouldNotRemoveUnauthorizedPost() {
        String postId = "id";
        User user = Instancio.of(User.class).create();
        Person person = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(user.getUsername())).thenReturn(person);
        when(postRepository.existsById(postId)).thenReturn(true);
        when(postRepository.existsByIdAndPerson(postId, person)).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> {
            postService.removePost(postId, user);
        });
    }

    @Test
    public void shouldReturnValidDTOS() {
        User user = Instancio.of(User.class).create();
        List<Post> posts = Instancio.ofList(Post.class)
                .size(10)
                .create();

        List<ReadPostDTO> postDTOS = posts.stream().map(postService::postToReadDTO).toList();

        when(postRepository.findAllByPersonUserUsername(user.getUsername(), 10, 0)).thenReturn(posts);

        List<ReadPostDTO> returnedPosts = postService.findPostsByUsername(user.getUsername(), 1);

        assertThat(returnedPosts)
                .usingRecursiveComparison()
                .isEqualTo(postDTOS);

    }

    @Test
    public void shouldReturnEvenWithoutPosts() {
        User user = Instancio.of(User.class).create();

        when(postRepository.findAllByPersonUserUsername(user.getUsername(), 10, 0)).thenReturn(new ArrayList<>());

        List<ReadPostDTO> returnedPosts = postService.findPostsByUsername(user.getUsername(), 1);

        assertThat(returnedPosts)
                .isNotNull();
    }
}