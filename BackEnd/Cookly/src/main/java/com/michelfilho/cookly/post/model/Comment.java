package com.michelfilho.cookly.post.model;

import com.michelfilho.cookly.person.model.Person;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Table(name = "comment")
@Entity
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, referencedColumnName = "id")
    private Person person;

    private String content;

    private Instant createdAt;

}
