package com.turkcell.library_app.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_app.dto.loan.CreateLoanRequest;
import com.turkcell.library_app.dto.loan.LoanResponse;
import com.turkcell.library_app.dto.loan.ReturnLoanRequest;
import com.turkcell.library_app.dto.loan.UpdateLoanStatusRequest;
import com.turkcell.library_app.enums.LoanStatus;
import com.turkcell.library_app.service.LoanService;

import org.springframework.http.HttpStatus;
import com.turkcell.library_app.dto.ApiResult;

@RestController
@RequestMapping("/api/v{version:1}/loans")
public class LoansController {
    private final LoanService loanService;

    public LoansController(LoanService loanService) {
        this.loanService = loanService;
    }


    @PostMapping
    public ApiResult<LoanResponse> create(@RequestBody CreateLoanRequest request) {
        LoanResponse response = loanService.create(request);
        return ApiResult.success(HttpStatus.CREATED.value(), "Loan created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResult<LoanResponse> getById(@PathVariable UUID id) {
        LoanResponse response = loanService.getById(id);
        return ApiResult.success("Loan retrieved successfully", response);
    }

    @GetMapping
    public ApiResult<Page<LoanResponse>> getAll(
        @RequestParam(required = false) UUID studentId,
        @RequestParam(required = false) LoanStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<LoanResponse> response = loanService.getAll(studentId, status, page, size);
        return ApiResult.success("Loans retrieved successfully", response);
    }

    @PatchMapping("/{id}/status")
    public ApiResult<LoanResponse> updateStatus(@PathVariable UUID id, @RequestBody UpdateLoanStatusRequest request) {
        LoanResponse response = loanService.updateStatus(id, request);
        return ApiResult.success("Loan status updated successfully", response);
    }

    @PostMapping("/{id}/return")
    public ApiResult<LoanResponse> returnLoan(@PathVariable UUID id, @RequestBody ReturnLoanRequest request) {
        LoanResponse response = loanService.returnLoan(id, request);
        return ApiResult.success("Loan returned successfully", response);
    }
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        loanService.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Loan deleted successfully");
    }
}
