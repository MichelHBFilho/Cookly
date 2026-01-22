package com.michelfilho.cookly.post.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public record ReadPostDTO(
        String id,
        ReadRecipeDTO recipe,
        List<CommentDTO> comments,
        Integer likes,
        String author,
        String description,
        Instant createdAt
) {
}
