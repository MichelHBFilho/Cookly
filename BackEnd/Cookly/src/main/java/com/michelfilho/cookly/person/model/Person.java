package com.michelfilho.cookly.person.model;

import com.michelfilho.cookly.authentication.model.User;
import com.michelfilho.cookly.person.dto.ReadPersonDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

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
    private String lastName;

    private String profilePicturePath;

    private LocalDate birthDay;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "person_following_id"),
            inverseJoinColumns = @JoinColumn(name = "person_followed_id")
    )
    private List<Person> following;

    @ManyToMany(mappedBy = "following")
    private List<Person> followers;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Person(String name, String lastName, String profilePicturePath, LocalDate birthDay, User user) {
        this.name = name;
        this.lastName = lastName;
        this.profilePicturePath = profilePicturePath;
        this.birthDay = birthDay;
        this.user = user;
    }

    public Person() {}

    public String getFullName() {
        return this.name + " " + this.lastName;
    }

    public String getProfilePictureName() {
        String[] profilePicturePath = this.getProfilePicturePath().split("/");
        return profilePicturePath[profilePicturePath.length - 1];
    }

    public ReadPersonDTO toReadPersonDTO() {
        return new ReadPersonDTO(
                this.getId(),
                this.getProfilePictureName(),
                this.getBirthDay(),
                this.getFullName(),
                this.getUser().getUsername(),
                this.getFollowers().size()
        );
    }

    public void startToFollow(Person person) {
        this.getFollowing().add(person);
    }
}
