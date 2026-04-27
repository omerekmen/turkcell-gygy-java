package com.turkcell.library_app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.library_app.dto.loan.CreateLoanRequest;
import com.turkcell.library_app.dto.loan.LoanResponse;
import com.turkcell.library_app.dto.loan.ReturnLoanRequest;
import com.turkcell.library_app.dto.loan.UpdateLoanStatusRequest;
import com.turkcell.library_app.entity.BookCopy;
import com.turkcell.library_app.entity.Loan;
import com.turkcell.library_app.entity.Return;
import com.turkcell.library_app.entity.Staff;
import com.turkcell.library_app.entity.Student;
import com.turkcell.library_app.enums.BookCopyStatus;
import com.turkcell.library_app.enums.LoanStatus;
import com.turkcell.library_app.enums.StaffRole;
import com.turkcell.library_app.repository.BookCopyRepository;
import com.turkcell.library_app.repository.LoanRepository;
import com.turkcell.library_app.repository.ReturnRepository;
import com.turkcell.library_app.repository.StaffRepository;
import com.turkcell.library_app.repository.StudentRepository;

@Service
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final BookCopyRepository bookCopyRepository;
    private final ReturnRepository returnRepository;

    public LoanServiceImpl(
        LoanRepository loanRepository,
        StudentRepository studentRepository,
        StaffRepository staffRepository,
        BookCopyRepository bookCopyRepository,
        ReturnRepository returnRepository
    ) {
        this.loanRepository = loanRepository;
        this.studentRepository = studentRepository;
        this.staffRepository = staffRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.returnRepository = returnRepository;
    }

    @Override
    public LoanResponse create(CreateLoanRequest request) {
        if (request.getStudentId() == null || request.getStaffId() == null || request.getDueDate() == null
            || request.getBookCopyIds() == null || request.getBookCopyIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "studentId, staffId, dueDate and bookCopyIds are required");
        }
        if (!request.getDueDate().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dueDate must be in the future");
        }

        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found"));
        if (!Boolean.TRUE.equals(student.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student is not active");
        }

        Staff staff = staffRepository.findById(request.getStaffId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Staff not found"));
        validateLoanOperator(staff);

        List<BookCopy> copies = bookCopyRepository.findByIdIn(request.getBookCopyIds());
        if (copies.size() != request.getBookCopyIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more book copies not found");
        }
        for (BookCopy copy : copies) {
            if (copy.getStatus() != BookCopyStatus.AVAILABLE) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Book copy is not available: " + copy.getId());
            }
        }

        Loan loan = new Loan();
        loan.setStudent(student);
        loan.setStaff(staff);
        loan.setLoanDate(LocalDateTime.now());
        loan.setDueDate(request.getDueDate());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setBookCopies(copies);

        for (BookCopy copy : copies) {
            copy.setStatus(BookCopyStatus.BORROWED);
        }
        bookCopyRepository.saveAll(copies);

        return map(loanRepository.save(loan));
    }

    @Override
    public LoanResponse getById(UUID id) {
        return map(findLoan(id));
    }

    @Override
    public Page<LoanResponse> getAll(UUID studentId, LoanStatus status, int page, int size) {
        if (studentId != null && status != null) {
            return loanRepository.findByStudentIdAndStatus(studentId, status, PageRequest.of(page, size)).map(this::map);
        }
        if (studentId != null) {
            return loanRepository.findByStudentId(studentId, PageRequest.of(page, size)).map(this::map);
        }
        if (status != null) {
            return loanRepository.findByStatus(status, PageRequest.of(page, size)).map(this::map);
        }
        return loanRepository.findAll(PageRequest.of(page, size)).map(this::map);
    }

    @Override
    public LoanResponse updateStatus(UUID id, UpdateLoanStatusRequest request) {
        if (request.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
        }
        Loan loan = findLoan(id);
        loan.setStatus(request.getStatus());
        return map(loanRepository.save(loan));
    }

    @Override
    public LoanResponse returnLoan(UUID id, ReturnLoanRequest request) {
        if (request.getProcessedByStaffId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "processedByStaffId is required");
        }
        Loan loan = findLoan(id);
        if (loan.getStatus() != LoanStatus.ACTIVE && loan.getStatus() != LoanStatus.OVERDUE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only active/overdue loans can be returned");
        }

        Staff processedBy = staffRepository.findById(request.getProcessedByStaffId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Processing staff not found"));
        validateLoanOperator(processedBy);

        Return loanReturn = new Return();
        loanReturn.setLoan(loan);
        loanReturn.setProcessedBy(processedBy);
        loanReturn.setReturnDate(LocalDateTime.now());
        returnRepository.save(loanReturn);

        for (BookCopy copy : loan.getBookCopies()) {
            copy.setStatus(BookCopyStatus.AVAILABLE);
        }
        bookCopyRepository.saveAll(loan.getBookCopies());

        loan.setStatus(LoanStatus.COMPLETED);
        return map(loanRepository.save(loan));
    }

    @Override
    public void delete(UUID id) {
        Loan loan = findLoan(id);
        loanRepository.delete(loan);
    }

    private void validateLoanOperator(Staff staff) {
        if (!Boolean.TRUE.equals(staff.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Staff is not active");
        }
        StaffRole role = staff.getRole();
        if (role != StaffRole.CIRCULATION_DESK && role != StaffRole.LIBRARIAN && role != StaffRole.MANAGER) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Staff role is not allowed for loan operations");
        }
    }

    private Loan findLoan(UUID id) {
        return loanRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan not found"));
    }

    private LoanResponse map(Loan loan) {
        LoanResponse response = new LoanResponse();
        response.setId(loan.getId());
        response.setStudentId(loan.getStudent() == null ? null : loan.getStudent().getId());
        response.setStaffId(loan.getStaff() == null ? null : loan.getStaff().getId());
        response.setLoanDate(loan.getLoanDate());
        response.setDueDate(loan.getDueDate());
        response.setStatus(loan.getStatus());
        List<UUID> copyIds = new ArrayList<>();
        if (loan.getBookCopies() != null) {
            for (BookCopy copy : loan.getBookCopies()) {
                copyIds.add(copy.getId());
            }
        }
        response.setBookCopyIds(copyIds);
        return response;
    }
}
