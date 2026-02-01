package com.michelfilho.cookly.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

public record ReadPostDTO(
        @Schema(example = "UUID")
        String id,
        @Schema(implementation = ReadRecipeDTO.class)
        ReadRecipeDTO recipe,
        @Schema(implementation = ReadCommentDTO.class)
        List<ReadCommentDTO> comments,
        @Schema(example = "40")
        Integer likes,
        @Schema(example = "Jane Doe")
        String author,
        @Schema(example = "Easy egg salad sandwich")
        String description,
        @Schema(example = "2025-11-20")
        Instant createdAt,
        List<String> imagesPaths
) {
}
