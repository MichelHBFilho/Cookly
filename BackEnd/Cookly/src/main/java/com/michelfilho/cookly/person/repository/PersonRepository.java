package com.michelfilho.cookly.person.repository;

import com.michelfilho.cookly.person.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, String> {

}
