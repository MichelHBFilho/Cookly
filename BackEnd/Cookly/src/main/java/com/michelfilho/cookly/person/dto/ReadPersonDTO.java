package com.michelfilho.cookly.person.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReadPersonDTO(
        String profilePictureURL,
        LocalDate birthDay,
        String fullName,
        String username
        ) {
}
