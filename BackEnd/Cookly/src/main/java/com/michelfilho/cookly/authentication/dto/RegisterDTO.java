package com.michelfilho.cookly.authentication.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record RegisterDTO(
        String username,
        String password,
        String name,
        String lastName,
        MultipartFile profilePicture,
        LocalDate birthDay) {
}
