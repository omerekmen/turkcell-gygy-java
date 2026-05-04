package com.turkcell.library_cqrs.api.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.api.dto.loan.CreateLoanRequest;
import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.api.dto.loan.ReturnLoanRequest;
import com.turkcell.library_cqrs.api.dto.loan.UpdateLoanStatusRequest;
import com.turkcell.library_cqrs.application.features.loan.command.create.CreateLoanCommand;
import com.turkcell.library_cqrs.application.features.loan.command.delete.DeleteLoanCommand;
import com.turkcell.library_cqrs.application.features.loan.command.returnloan.ReturnLoanCommand;
import com.turkcell.library_cqrs.application.features.loan.command.update.UpdateLoanStatusCommand;
import com.turkcell.library_cqrs.application.features.loan.query.getall.GetLoansQuery;
import com.turkcell.library_cqrs.application.features.loan.query.getbyid.GetLoanByIdQuery;
import com.turkcell.library_cqrs.core.mediator.Mediator;

@RestController
@RequestMapping("api/v{version:1}/loans")
public class LoansController {

    private final Mediator mediator;

    public LoansController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<ApiResult<LoanResponse>> create(@RequestBody CreateLoanRequest request) {
        var dto = mediator.send(new CreateLoanCommand(
            request.getStudentId(),
            request.getStaffId(),
            request.getDueDate(),
            request.getBookCopyIds()
        ));

        return ResponseEntity.created(URI.create("/loans/" + dto.getId()))
            .body(ApiResult.success(HttpStatus.CREATED.value(), "Loan created successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<LoanResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResult.success("Loan retrieved successfully", mediator.send(new GetLoanByIdQuery(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResult<Page<LoanResponse>>> getAll(
        @RequestParam(required = false) UUID studentId,
        @RequestParam(required = false) com.turkcell.library_cqrs.domain.enums.LoanStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResult.success("Loans retrieved successfully", mediator.send(new GetLoansQuery(studentId, status, page, size))));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResult<LoanResponse>> updateStatus(@PathVariable UUID id, @RequestBody UpdateLoanStatusRequest request) {
        return ResponseEntity.ok(ApiResult.success("Loan status updated successfully", mediator.send(new UpdateLoanStatusCommand(id, request.getStatus()))));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResult<LoanResponse>> returnLoan(@PathVariable UUID id, @RequestBody ReturnLoanRequest request) {
        return ResponseEntity.ok(ApiResult.success("Loan returned successfully", mediator.send(new ReturnLoanCommand(id, request.getProcessedByStaffId()))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable UUID id) {
        mediator.send(new DeleteLoanCommand(id));
        return ResponseEntity.ok(ApiResult.success(HttpStatus.NO_CONTENT.value(), "Loan deleted successfully"));
    }
}
