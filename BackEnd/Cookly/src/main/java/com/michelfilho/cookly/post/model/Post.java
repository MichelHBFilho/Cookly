package com.michelfilho.cookly.post.model;

import com.michelfilho.cookly.person.model.Person;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "post")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", nullable = false)
    private Person person;

    private String description;

    private List<String> imagesPaths;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private List<PostLike> postLikes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "post")
    private List<Comment> comments;

    private final Instant createdAt = Instant.now();

    public Post(Recipe recipe, Person person, String description, List<String> imagesPaths) {
        this.recipe = recipe;
        this.person = person;
        this.description = description;
        this.imagesPaths = imagesPaths;
    }

    public void addLike(Person person) {
        this.postLikes.add(new PostLike(
                person,
                this
        ));
    }

    public void addComment(Person person, String text) {
        this.comments.add(new Comment(
                this,
                person,
                text
        ));
    }
}
