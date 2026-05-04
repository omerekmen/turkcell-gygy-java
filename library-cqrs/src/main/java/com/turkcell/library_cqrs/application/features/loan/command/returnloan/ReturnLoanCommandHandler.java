package com.turkcell.library_cqrs.application.features.loan.command.returnloan;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.application.features.loan.mapper.LoanMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.domain.entity.Return;
import com.turkcell.library_cqrs.domain.enums.LoanStatus;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.LoanRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ReturnRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StaffRepository;

@Component
public class ReturnLoanCommandHandler implements CommandHandler<ReturnLoanCommand, LoanResponse> {

    private final LoanRepository loanRepository;
    private final ReturnRepository returnRepository;
    private final StaffRepository staffRepository;

    public ReturnLoanCommandHandler(LoanRepository loanRepository, ReturnRepository returnRepository, StaffRepository staffRepository) {
        this.loanRepository = loanRepository;
        this.returnRepository = returnRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public LoanResponse handle(ReturnLoanCommand command) {
        var loan = loanRepository.findById(command.loanId())
            .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        var staff = staffRepository.findById(command.processedByStaffId())
            .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        var returnEntity = new Return();
        returnEntity.setLoan(loan);
        returnEntity.setProcessedBy(staff);
        returnEntity.setReturnDate(LocalDateTime.now());
        returnRepository.save(returnEntity);

        loan.setStatus(LoanStatus.COMPLETED);
        return LoanMapper.toDto(loanRepository.save(loan));
    }
}
