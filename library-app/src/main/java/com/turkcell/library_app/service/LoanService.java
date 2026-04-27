package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_app.dto.loan.CreateLoanRequest;
import com.turkcell.library_app.dto.loan.LoanResponse;
import com.turkcell.library_app.dto.loan.ReturnLoanRequest;
import com.turkcell.library_app.dto.loan.UpdateLoanStatusRequest;
import com.turkcell.library_app.enums.LoanStatus;

public interface LoanService {
    LoanResponse create(CreateLoanRequest request);
    LoanResponse getById(UUID id);
    Page<LoanResponse> getAll(UUID studentId, LoanStatus status, int page, int size);
    LoanResponse updateStatus(UUID id, UpdateLoanStatusRequest request);
    LoanResponse returnLoan(UUID id, ReturnLoanRequest request);
    void delete(UUID id);
}
