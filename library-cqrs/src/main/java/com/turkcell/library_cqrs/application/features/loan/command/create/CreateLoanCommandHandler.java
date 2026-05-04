package com.turkcell.library_cqrs.application.features.loan.command.create;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.loan.LoanResponse;
import com.turkcell.library_cqrs.application.features.loan.mapper.LoanMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.domain.entity.Loan;
import com.turkcell.library_cqrs.domain.enums.LoanStatus;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookCopyRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.LoanRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StaffRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StudentRepository;

@Component
public class CreateLoanCommandHandler implements CommandHandler<CreateLoanCommand, LoanResponse> {

    private final LoanRepository loanRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final BookCopyRepository bookCopyRepository;

    public CreateLoanCommandHandler(
        LoanRepository loanRepository,
        StudentRepository studentRepository,
        StaffRepository staffRepository,
        BookCopyRepository bookCopyRepository
    ) {
        this.loanRepository = loanRepository;
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    public LoanResponse handle(CreateLoanCommand command) {
        var entity = new Loan();

        entity.setStudent(studentRepository.findById(command.studentId())
            .orElseThrow(() -> new IllegalArgumentException("Student not found")));
        entity.setStaff(staffRepository.findById(command.staffId())
            .orElseThrow(() -> new IllegalArgumentException("Staff not found")));
        entity.setLoanDate(LocalDateTime.now());
        entity.setDueDate(command.dueDate());
        entity.setStatus(LoanStatus.ACTIVE);

        List<com.turkcell.library_cqrs.domain.entity.BookCopy> copies = command.bookCopyIds() == null
            ? List.of()
            : bookCopyRepository.findAllById(command.bookCopyIds());
        entity.setBookCopies(copies);

        var saved = loanRepository.save(entity);
        return LoanMapper.toDto(saved);
    }
}
