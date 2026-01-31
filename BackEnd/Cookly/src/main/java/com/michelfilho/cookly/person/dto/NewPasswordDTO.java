package com.michelfilho.cookly.person.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record NewPasswordDTO(
        @Schema(example = "weakPassword")
        String oldPassword,
        @Schema(example = "strongP4ssw0rd")
        String newPassword
) {
}
