package com.michelfilho.cookly.authentication.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record RegisterDTO(
        String username,
        String password,
        String name,
        String surName,
        MultipartFile profilePicture,
        LocalDate birthDay) {
}
