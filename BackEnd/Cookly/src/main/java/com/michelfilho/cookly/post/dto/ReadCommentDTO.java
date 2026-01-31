package com.michelfilho.cookly.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record ReadCommentDTO(
        @Schema(example = "UUID")
        String id,
        @Schema(example = "johhnyDoe")
        String author,
        @Schema(example = "Very good recipe!")
        String text,
        @Schema(example = "2025-12-28")
        Instant createdAt
){
}
