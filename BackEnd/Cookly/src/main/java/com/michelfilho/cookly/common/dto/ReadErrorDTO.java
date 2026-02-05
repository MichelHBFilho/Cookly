package com.michelfilho.cookly.common.dto;

public record ReadErrorDTO(
        Integer code,
        String errorDescription
) {
}
