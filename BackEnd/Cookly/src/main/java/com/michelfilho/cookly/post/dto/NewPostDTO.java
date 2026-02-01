package com.michelfilho.cookly.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record NewPostDTO(
        @NotBlank
        @NotNull
        @Schema(example = "Love Apple")
        String recipeName,
        @Size(max=150)
        @Schema(example = "A sugared apple")
        String description,
        @Min(1)
        @Schema(example = "15")
        Integer prepareTime,
        @Size(min=1, max=15)
        @Schema(example = "[\"turn on the oven\", \"turn off the oven\"]")
        List<String> stepsToPrepare
) {
}
