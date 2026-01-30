package com.michelfilho.cookly.authentication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record RegisterDTO(
        @Schema(example = "johnnyDoe")
        String username,
        @Schema(example = "password")
        String password,
        @Schema(example = "John")
        String name,
        @Schema(example = "Doe")
        String lastName,
        @Schema(example = "IMAGE")
        MultipartFile profilePicture,
        @Schema(example = "2000-01-01")
        LocalDate birthDay) {
}
