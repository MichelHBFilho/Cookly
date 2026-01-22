package com.michelfilho.cookly.post.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public record CommentDTO(
        String author,
        String text,
        Instant createdAt
){
}
