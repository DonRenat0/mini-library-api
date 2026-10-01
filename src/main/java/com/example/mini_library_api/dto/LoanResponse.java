package com.example.mini_library_api.dto;

import java.time.LocalDate;

public record LoanResponse(
        Long id,
        String bookTitle,
        String memberName,
        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnedDate
) {
}