package com.michelfilho.cookly.person.service;

import com.michelfilho.cookly.common.exception.InvalidInteractionStateException;
import com.michelfilho.cookly.common.exception.NotFoundException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InteractPersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private InteractPersonService service;

    @Test
    public void shouldFollow_PerfectSituation() {
        Person personA = Instancio.of(Person.class).create();
        Person personB = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(personA.getUser().getUsername())).thenReturn(personA);
        when(personRepository. findByUserUsername(personB.getUser().getUsername())).thenReturn(personB);

        when(personRepository.existsByUser_UsernameAndFollowing_User_Username(personA.getUser().getUsername(), personB.getUser().getUsername())).thenReturn(false);

        service.follow(personA.getUser(), personB.getUser().getUsername());

        assertThat(personA.getFollowing().contains(personB));
        verify(personRepository).save(personA);
    }

    @Test
    public void shouldNotFollow_AlreadyFollows() {
        Person personA = Instancio.of(Person.class).create();
        Person personB = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(personA.getUser().getUsername())).thenReturn(personA);
        when(personRepository. findByUserUsername(personB.getUser().getUsername())).thenReturn(personB);

        when(personRepository.existsByUser_UsernameAndFollowing_User_Username(personA.getUser().getUsername(), personB.getUser().getUsername())).thenReturn(true);

        assertThrows(InvalidInteractionStateException.class, () -> service.follow(personA.getUser(), personB.getUser().getUsername()));
    }

    @Test
    public void shouldNotFollow_Inexistent() {
        Person personA = Instancio.of(Person.class).create();
        Person personB = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(personA.getUser().getUsername())).thenReturn(personA);
        when(personRepository. findByUserUsername(personB.getUser().getUsername())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.follow(personA.getUser(), personB.getUser().getUsername()));
    }

    @Test
    public void shouldUnfollow_PerfectSituation() {
        Person personA = Instancio.of(Person.class).create();
        Person personB = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(personA.getUser().getUsername())).thenReturn(personA);
        when(personRepository. findByUserUsername(personB.getUser().getUsername())).thenReturn(personB);

        when(personRepository.existsByUser_UsernameAndFollowing_User_Username(personA.getUser().getUsername(), personB.getUser().getUsername())).thenReturn(true);

        service.unfollow(personA.getUser(), personB.getUser().getUsername());

        assertThat(!personA.getFollowing().contains(personB));
        verify(personRepository).save(personA);
    }

    @Test
    public void shouldNotUnfollow_NotFollows() {
        Person personA = Instancio.of(Person.class).create();
        Person personB = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(personA.getUser().getUsername())).thenReturn(personA);
        when(personRepository. findByUserUsername(personB.getUser().getUsername())).thenReturn(personB);

        when(personRepository.existsByUser_UsernameAndFollowing_User_Username(personA.getUser().getUsername(), personB.getUser().getUsername())).thenReturn(false);

        assertThrows(InvalidInteractionStateException.class, () -> service.unfollow(personA.getUser(), personB.getUser().getUsername()));
    }

    @Test
    public void shouldNotUnfollow_Inexistent() {
        Person personA = Instancio.of(Person.class).create();
        Person personB = Instancio.of(Person.class).create();

        when(personRepository.findByUserUsername(personA.getUser().getUsername())).thenReturn(personA);
        when(personRepository. findByUserUsername(personB.getUser().getUsername())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> service.unfollow(personA.getUser(), personB.getUser().getUsername()));
    }
}
