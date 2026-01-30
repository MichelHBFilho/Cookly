package com.michelfilho.cookly.person.service;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.exception.NotFoundException;
import com.michelfilho.cookly.common.exception.UnauthorizedException;
import com.michelfilho.cookly.common.service.ImageService;
import com.michelfilho.cookly.person.dto.ReadPersonDTO;
import com.michelfilho.cookly.person.dto.UpdatePersonDTO;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.michelfilho.cookly.person.service.PersonField.*;

@Service
public class PersonService {

    @Autowired
    private ImageService imageService;
    @Autowired
    private PersonRepository personRepository;
    @Value("${api.storage.pictures.profile.path}")
    private String profilePicturesPath;


    public ReadPersonDTO getPersonInformation(String username) {
        Person person = personRepository.findByUserUsername(username);

        if(person == null) throw new NotFoundException(Person.class);

        String[] profilePicturePath = person.getProfilePicturePath().split("/");
        String profilePictureName = profilePicturePath[profilePicturePath.length - 1];
        return new ReadPersonDTO(
                profilePictureName,
                person.getBirthDay(),
                person.getFullName(),
                person.getUser().getUsername()
        );
    }

    public void updatePersonInformation(
            UpdatePersonDTO data,
            User user
    ) {
        Person person = personRepository.findByUserUsername(user.getUsername());

        applyUpdates(data, person);

        personRepository.save(person);
    }

    private void applyUpdates(UpdatePersonDTO data, Person person) {
        if(data.profilePicture() != null) {
            updateProfilePicture(person, data.profilePicture());
        }
        if(data.lastName() != null) {
            person.setLastName(data.lastName());
        }
        if(data.firstName() != null) {
            person.setName(data.firstName());
        }
        if(data.birthDay() != null) {
            person.setBirthDay(data.birthDay());
        }
    }

    private void updateProfilePicture(Person person, MultipartFile image) {
        imageService.deleteImage(profilePicturesPath + "/" + person.getProfilePicturePath());
        imageService.saveImage(profilePicturesPath, image);
    }

}

enum PersonField {
    FIRST_NAME,
    LAST_NAME,
    PROFILE_PICTURE,
    BIRTHDAY
}
