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

@RestController
@RequestMapping("/api/v{version:1}/loans")
public class LoansController {
    private final LoanService loanService;

    public LoansController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping
    public LoanResponse create(@RequestBody CreateLoanRequest request) {
        return loanService.create(request);
    }

    @GetMapping("/{id}")
    public LoanResponse getById(@PathVariable UUID id) {
        return loanService.getById(id);
    }

    @GetMapping
    public Page<LoanResponse> getAll(
        @RequestParam(required = false) UUID studentId,
        @RequestParam(required = false) LoanStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return loanService.getAll(studentId, status, page, size);
    }

    @PatchMapping("/{id}/status")
    public LoanResponse updateStatus(@PathVariable UUID id, @RequestBody UpdateLoanStatusRequest request) {
        return loanService.updateStatus(id, request);
    }

    @PostMapping("/{id}/return")
    public LoanResponse returnLoan(@PathVariable UUID id, @RequestBody ReturnLoanRequest request) {
        return loanService.returnLoan(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        loanService.delete(id);
    }
}
