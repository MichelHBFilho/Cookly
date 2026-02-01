package com.michelfilho.cookly.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDTO(
        @Schema(example = "johnnyDoe")
        String username,
        @Schema(example = "password")
        String password) {
}
