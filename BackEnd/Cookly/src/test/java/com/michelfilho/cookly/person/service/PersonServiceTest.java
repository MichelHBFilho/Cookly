package com.michelfilho.cookly.person.service;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.service.ImageService;
import com.michelfilho.cookly.person.dto.UpdatePersonDTO;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import org.apache.commons.io.FileUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ImageService imageService = new ImageService();

    @InjectMocks
    private PersonService personService;

    private static String dir = "src/main/test/resources/";

    @AfterAll
    public static void deleteTestFolder() throws IOException {
        FileUtils.deleteDirectory(new File(dir));
    }

    @BeforeAll
    public static void setup() throws IOException {
        Files.createDirectories(Path.of(dir));
    }

    @Test
    public void shouldUpdatePersonOrdinaryInformation() {
        UpdatePersonDTO updatePersonDTO = Instancio.of(UpdatePersonDTO.class)
                .set(field("profilePicture"), null)
                .create();

        User user = Instancio.of(User.class)
                .create();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        when(personRepository.findByUserUsername(user.getUsername()))
                .thenReturn(person);

        personService.updatePersonInformation(
                updatePersonDTO,
                user,
                user.getUsername()
        );

        verify(personRepository).save(person);

    }

    @Test
    public void shouldUpdatePersonImage() throws IOException {

        User user = Instancio.of(User.class)
                .create();

        Person person = Instancio.of(Person.class)
                .set(field("user"), user)
                .create();

        byte[] inputArray = "Test String".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", inputArray);
        Path path = Path.of(dir).resolve("fileName");
        Files.copy(mockMultipartFile.getInputStream(), path);

        inputArray = "New Test String".getBytes();
        mockMultipartFile = new MockMultipartFile("fileName", inputArray);

        UpdatePersonDTO updatePersonDTO = new UpdatePersonDTO(
                null,
                null,
                mockMultipartFile,
                LocalDate.now()
        );

        when(personRepository.findByUserUsername(user.getUsername()))
                .thenReturn(person);

        personService.updatePersonInformation(
                updatePersonDTO,
                user,
                user.getUsername()
        );

        verify(personRepository).save(person);

        Assertions.assertNotEquals("fileName", person.getProfilePicturePath());

    }

}