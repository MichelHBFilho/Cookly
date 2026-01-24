package com.michelfilho.cookly.person.service;

import ch.qos.logback.core.util.StringUtil;
import com.michelfilho.cookly.common.exception.NotFound;
import com.michelfilho.cookly.common.service.ImageService;
import com.michelfilho.cookly.person.dto.ReadPersonDTO;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;

@Service
public class PersonService {

    @Autowired
    private ImageService imageService;
    @Autowired
    private PersonRepository personRepository;


    public ReadPersonDTO getPersonInformation(String username) {
        Person person = personRepository.findByUserUsername(username);

        if(person == null) throw new NotFound(Person.class);

        String[] profilePicturePath = person.getProfilePicturePath().split("/");
        String profilePictureName = profilePicturePath[profilePicturePath.length - 1];
        return new ReadPersonDTO(
                profilePictureName,
                person.getBirthDay(),
                person.getFullName(),
                person.getUser().getUsername()
        );
    }

}
