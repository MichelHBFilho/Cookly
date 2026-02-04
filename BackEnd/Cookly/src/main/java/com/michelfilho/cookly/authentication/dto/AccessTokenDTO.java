package com.michelfilho.cookly.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AccessTokenDTO(
        @Schema(example = "Bearer Token")
        String token
) {
}
