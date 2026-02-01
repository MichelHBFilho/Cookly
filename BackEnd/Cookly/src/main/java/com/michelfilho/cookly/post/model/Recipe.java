package com.michelfilho.cookly.post.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "recipe")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "char(36)")
    private String id;
    private String name;
    private Integer prepareTime;

    @OneToMany(
            mappedBy = "recipe",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("stepOrder ASC")
    private List<StepToPrepare> stepByStep;

    public Recipe(List<StepToPrepare> stepByStep, Integer prepareTime, String name) {
        this.stepByStep = stepByStep;
        this.prepareTime = prepareTime;
        this.name = name;
    }

    public void addStep(StepToPrepare step) {
        step.setRecipe(this);
        this.stepByStep.add(step);
    }

    public void addListOfStringSteps(List<String> steps) {
        for (int i = 1; i <= steps.size(); i++) {
            String stepToPrepareIterator = steps.get(i-1);
            this.addStep(new StepToPrepare(
                    i,
                    stepToPrepareIterator
            ));
        }
    }
}
