package com.michelfilho.cookly.person.model;

import com.michelfilho.cookly.authentication.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "person")
@Entity
@Setter
@Getter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    private String id;

    private String name;
    private String surName;

    private String profilePicturePath;

    private LocalDate birthDay;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;


    public Person(String name, String surName, String profilePicturePath, LocalDate birthDay, User user) {
        this.name = name;
        this.surName = surName;
        this.profilePicturePath = profilePicturePath;
        this.birthDay = birthDay;
        this.user = user;
    }

    public Person() {}

    public String getFullName() {
        return this.name + " " + this.surName;
    }
}
