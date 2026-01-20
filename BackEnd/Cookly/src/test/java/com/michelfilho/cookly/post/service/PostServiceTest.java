package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.CooklyApplication;
import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.model.Post;
import com.michelfilho.cookly.post.model.Recipe;
import com.michelfilho.cookly.post.model.StepToPrepare;
import com.michelfilho.cookly.post.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        // Create the post
        NewPostDTO dto = new NewPostDTO(
                "RecipeName",
                "This is my recipe",
                15,
                List.of("Cook", "Uncook")
        );
        Person person = new Person();
        User user = new User("TestUser", "password");
        when(personRepository.findByUserUsername("TestUser"))
                .thenReturn(person);
        postService.publishAPost(dto, user);

        // Get the saved post
        ArgumentCaptor<Post> postArgumentCaptor = ArgumentCaptor.forClass(Post.class);

        verify(postRepository).save(postArgumentCaptor.capture());

        Post savedPost = postArgumentCaptor.getValue();

        // Assert
        assertAll(
                () -> assertEquals(person, savedPost.getPerson()),
                () -> assertEquals("This is my recipe", savedPost.getDescription()),

                () -> {
                    Recipe recipe = savedPost.getRecipe();

                    assertAll(
                            () -> assertEquals("RecipeName", recipe.getName()),
                            () -> assertEquals(15, recipe.getPrepareTime()),
                            () -> assertEquals(2, recipe.getStepByStep().size()),

                            () -> assertEquals(1, recipe.getStepByStep().get(0).getStepOrder()),
                            () -> assertEquals("Cook", recipe.getStepByStep().get(0).getDescription()),

                            () -> assertEquals(2, recipe.getStepByStep().get(1).getStepOrder()),
                            () -> assertEquals("Uncook", recipe.getStepByStep().get(1).getDescription())
                    );
                }
        );
    }
}