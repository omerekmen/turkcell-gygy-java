package com.turkcell.library_cqrs.application.features.reservation.command.create;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.application.features.reservation.mapper.ReservationMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.domain.entity.Reservation;
import com.turkcell.library_cqrs.domain.enums.BookCopyStatus;
import com.turkcell.library_cqrs.domain.enums.ReservationStatus;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookCopyRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.BookRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ReservationRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StudentRepository;

@Component
public class CreateReservationCommandHandler implements CommandHandler<CreateReservationCommand, ReservationResponse> {

    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public CreateReservationCommandHandler(
        ReservationRepository reservationRepository,
        StudentRepository studentRepository,
        BookRepository bookRepository,
        BookCopyRepository bookCopyRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.studentRepository = studentRepository;
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    public ReservationResponse handle(CreateReservationCommand command) {
        if (command.studentId() == null || command.bookId() == null) {
            throw new IllegalArgumentException("studentId and bookId are required");
        }

        var student = studentRepository.findById(command.studentId())
            .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        if (!Boolean.TRUE.equals(student.getIsActive())) {
            throw new IllegalArgumentException("Student is not active");
        }

        var book = bookRepository.findById(command.bookId())
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getDeletedAt() != null) {
            throw new IllegalArgumentException("Book is deleted");
        }

        long availableCount = bookCopyRepository.countByBookIdAndStatus(book.getId(), BookCopyStatus.AVAILABLE);
        if (availableCount > 0) {
            throw new IllegalArgumentException("Book has available copies, reservation is not needed");
        }

        var reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setExpiresAt(command.expiresAt() == null ? LocalDateTime.now().plusDays(3) : command.expiresAt());
        long queueSize = reservationRepository.countByBookIdAndStatus(book.getId(), ReservationStatus.PENDING);
        reservation.setPosition((int) queueSize + 1);

        return ReservationMapper.toDto(reservationRepository.save(reservation));
    }
}
