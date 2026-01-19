package com.michelfilho.cookly.post.dto;

import java.util.List;

public record NewPostDTO(
    String recipeName,
    Integer prepareTime,
    List<String> stepsToPrepare
) {
}
