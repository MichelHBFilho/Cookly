package com.michelfilho.cookly.authentication.dto;

import java.time.LocalDate;

public record RegisterDTO(
        String username,
        String password,
        String name,
        String surName,
        String profilePicturePath,
        LocalDate birthDay) {
}
