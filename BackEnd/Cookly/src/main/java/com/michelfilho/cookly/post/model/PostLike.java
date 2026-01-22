package com.michelfilho.cookly.post.model;

import com.michelfilho.cookly.person.model.Person;
import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "post_like")
@Entity
@Getter
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, referencedColumnName = "id")
    private Person person;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "id")
    private Post post;

    public PostLike(Person person, Post post) {
        this.person = person;
        this.post = post;
    }

    public PostLike() {}
}
