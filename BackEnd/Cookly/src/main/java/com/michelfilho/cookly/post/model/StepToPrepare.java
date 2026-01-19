package com.michelfilho.cookly.post.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "step_to_prepare")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StepToPrepare {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    private String id;
    private Integer stepOrder;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", referencedColumnName = "id", nullable = false)
    private Recipe recipe;

    public StepToPrepare(Integer stepOrder, String description) {
        this.stepOrder = stepOrder;
        this.description = description;
    }
}
