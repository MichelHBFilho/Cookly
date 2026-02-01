package com.michelfilho.cookly.person.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record UpdatePersonDTO(
        @Schema(example = "Jonathan", nullable = true)
        String firstName,
        @Schema(example = "null", nullable = true)
        String lastName,
        @Schema(example = "null", nullable = true)
        MultipartFile profilePicture,
        @Schema(example = "null", nullable = true)
        LocalDate birthDay
) {
}
