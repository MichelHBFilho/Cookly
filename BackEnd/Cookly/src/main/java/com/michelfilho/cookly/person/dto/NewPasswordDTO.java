package com.michelfilho.cookly.person.dto;

public record NewPasswordDTO(
        String oldPassword,
        String newPassword
) {
}
