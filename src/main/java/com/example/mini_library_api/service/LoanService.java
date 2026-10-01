package com.example.mini_library_api.service;

import com.example.mini_library_api.exception.BookNotAvailableException;
import com.example.mini_library_api.exception.ResourceNotFoundException;
import com.example.mini_library_api.model.Book;
import com.example.mini_library_api.model.Loan;
import com.example.mini_library_api.model.Member;
import com.example.mini_library_api.repository.BookRepository;
import com.example.mini_library_api.repository.LoanRepository;
import com.example.mini_library_api.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoanService {

    private static final int LOAN_DAYS = 14;

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Loan createLoan(Long bookId, Long memberId) {
        Book book = bookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado: " + bookId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado: " + memberId));

        long activeLoans = loanRepository.countByBookIdAndReturnedDateIsNull(bookId);

        if (activeLoans >= book.getTotalCopies()) {
            throw new BookNotAvailableException(
                    "No quedan copias disponibles de \"%s\"".formatted(book.getTitle())
            );
        }

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setMember(member);
        loan.setLoanDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(LOAN_DAYS));

        return loanRepository.save(loan);
    }

    @Transactional
    public Loan returnLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado: " + loanId));

        if (loan.getReturnedDate() != null) {
            throw new IllegalStateException("Este préstamo ya fue devuelto");
        }

        loan.setReturnedDate(LocalDate.now());
        return loan;
    }
}