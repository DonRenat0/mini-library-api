package com.example.mini_library_api.repository;

import com.example.mini_library_api.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    long countByBookIdAndReturnedDateIsNull(Long bookId);

    @Query("select l from Loan l where l.member.id = :memberId and l.returnedDate is null")
    List<Loan> findActiveLoansByMember(@Param("memberId") Long memberId);

    List<Loan> findByReturnedDateIsNullAndDueDateBefore(java.time.LocalDate date);
}