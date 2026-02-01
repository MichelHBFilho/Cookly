package com.michelfilho.cookly.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record ReadRecipeDTO(
        @Schema(example = "Egg sandwich")
        String name,
        @Schema(example = "15")
        Integer prepareTime,
        List<String> stepByStep
) {
}
