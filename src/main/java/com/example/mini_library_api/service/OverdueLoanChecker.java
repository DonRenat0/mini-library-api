package com.example.mini_library_api.service;

import com.example.mini_library_api.model.Loan;
import com.example.mini_library_api.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OverdueLoanChecker {

    private final LoanRepository loanRepository;

    @Scheduled(cron = "0 0 8 * * *")
    public void checkOverdueLoans() {
        List<Loan> overdueLoans = loanRepository.findByReturnedDateIsNullAndDueDateBefore(LocalDate.now());

        if (overdueLoans.isEmpty()) {
            log.info("No hay préstamos vencidos hoy.");
            return;
        }

        log.warn("Hay {} préstamo(s) vencido(s):", overdueLoans.size());
        for (Loan loan : overdueLoans) {
            log.warn(" - Préstamo #{}: \"{}\" prestado a {} (vencía el {})",
                    loan.getId(), loan.getBook().getTitle(), loan.getMember().getName(), loan.getDueDate());
        }
    }
}