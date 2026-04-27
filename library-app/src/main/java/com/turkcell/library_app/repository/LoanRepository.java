package com.turkcell.library_app.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.turkcell.library_app.entity.Loan;
import com.turkcell.library_app.enums.LoanStatus;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {
    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);
    Page<Loan> findByStudentId(UUID studentId, Pageable pageable);
    Page<Loan> findByStudentIdAndStatus(UUID studentId, LoanStatus status, Pageable pageable);
}
