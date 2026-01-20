package com.michelfilho.cookly.post.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewPostDTO(
        @NotBlank
        String recipeName,
        @Size(max=150)
        String description,
        @Min(1)
        Integer prepareTime,
        @Size(min=1, max=15)
        List<String> stepsToPrepare
) {
}
