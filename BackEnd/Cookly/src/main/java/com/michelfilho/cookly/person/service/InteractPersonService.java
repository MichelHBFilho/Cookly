package com.michelfilho.cookly.person.service;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.common.exception.InvalidInteractionStateException;
import com.michelfilho.cookly.common.exception.NotFoundException;
import com.michelfilho.cookly.person.model.Person;
import com.michelfilho.cookly.person.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InteractPersonService {

    @Autowired
    private PersonRepository personRepository;

    public void follow(User user, String willFollowUsername) {
        Person person = personRepository.findByUserUsername(user.getUsername());
        Person willFollow = personRepository.findByUserUsername(willFollowUsername);

        if(person == null || willFollow == null)
            throw new NotFoundException(Person.class);

        if(isFollowing(user, willFollowUsername)) {
            throw new InvalidInteractionStateException("You already follow this user");
        }

        person.getFollowing().add(willFollow);

        personRepository.save(person);
    }

    public void unfollow(User user, String willUnfollowUsername) {
        Person person = personRepository.findByUserUsername(user.getUsername());
        Person willFollow = personRepository.findByUserUsername(willUnfollowUsername);

        if(person == null || willFollow == null)
            throw new NotFoundException(Person.class);

        if(!isFollowing(user, willUnfollowUsername)) {
            throw new InvalidInteractionStateException("You don't follow this user");
        }


        person.getFollowing().remove(willFollow);

        personRepository.save(person);
    }

    public boolean isFollowing(User user, String checkUsername) {
        return personRepository.existsByUser_UsernameAndFollowing_User_Username(user.getUsername(), checkUsername);
    }
}
