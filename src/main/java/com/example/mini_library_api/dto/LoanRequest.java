package com.example.mini_library_api.dto;

import jakarta.validation.constraints.NotNull;

public record LoanRequest(
        @NotNull(message = "El id del libro es obligatorio")
        Long bookId,

        @NotNull(message = "El id del socio es obligatorio")
        Long memberId
) {
}