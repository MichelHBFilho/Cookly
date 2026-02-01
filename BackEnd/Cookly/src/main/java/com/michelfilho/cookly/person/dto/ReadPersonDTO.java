package com.michelfilho.cookly.person.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReadPersonDTO(
        @Schema(example = "DefaultPicture.png")
        String profilePictureURL,
        @Schema(example = "2007-10-02")
        LocalDate birthDay,
        @Schema(example = "John Doe")
        String fullName,
        @Schema(example = "johnnyDoe")
        String username
) {
}
