package com.michelfilho.cookly.person.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record UpdatePersonDTO(
        String firstName,
        String lastName,
        MultipartFile profilePicture,
        LocalDate birthDay
) {
}
