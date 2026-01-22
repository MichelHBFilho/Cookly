package com.michelfilho.cookly.post.dto;

import java.time.Instant;

public record ReadCommentDTO(
        String author,
        String text,
        Instant createdAt
){
}
