package com.michelfilho.cookly.post.service;

import com.michelfilho.cookly.CooklyApplication;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import com.michelfilho.cookly.post.dto.NewPostDTO;
import com.michelfilho.cookly.post.model.Post;
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
    void publishAPost() {
        NewPostDTO dto = new NewPostDTO(
                "Bolo de laranja",
                15,
                List.of("Ligar o forno", "Colocar o bolo", "Retirar do forno")
        );

        Person person = mock(Person.class);

        when(userDetails.getUsername()).thenReturn("michel");
        when(personRepository.findByUserUsername("michel")).thenReturn(person);

        ArgumentCaptor<Post> postCaptor = ArgumentCaptor.forClass(Post.class);

        postService.publishAPost(dto, userDetails);

        verify(personRepository).findByUserUsername("michel");
        verify(postRepository).save(postCaptor.capture());

        Post savedPost = postCaptor.getValue();

        assertThat(savedPost.getPerson()).isEqualTo(person);

        assertThat(savedPost.getRecipe().getPrepareTime()).isEqualTo(15);
        assertThat(savedPost.getRecipe().getName()).isEqualTo("Bolo de laranja");

        List<StepToPrepare> steps = savedPost.getRecipe().getStepByStep();
        assertThat(steps).hasSize(3);

        assertThat(steps.get(0).getStepOrder()).isEqualTo(1);
        assertThat(steps.get(0).getDescription()).isEqualTo("Ligar o forno");

        assertThat(steps.get(1).getStepOrder()).isEqualTo(2);
        assertThat(steps.get(1).getDescription()).isEqualTo("Colocar o bolo");

        assertThat(steps.get(2).getStepOrder()).isEqualTo(3);
        assertThat(steps.get(2).getDescription()).isEqualTo("Retirar do forno");

    }
}