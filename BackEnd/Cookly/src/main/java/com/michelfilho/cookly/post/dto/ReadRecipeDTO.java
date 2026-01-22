package com.michelfilho.cookly.post.dto;

import java.util.List;

public record ReadRecipeDTO(
        String name,
        Integer prepareTime,
        List<String> stepByStep
) {
}
