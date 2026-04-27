package com.turkcell.library_app.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.library_app.dto.reservation.CreateReservationRequest;
import com.turkcell.library_app.dto.reservation.ReservationResponse;
import com.turkcell.library_app.dto.reservation.UpdateReservationStatusRequest;
import com.turkcell.library_app.entity.Book;
import com.turkcell.library_app.entity.Reservation;
import com.turkcell.library_app.entity.Student;
import com.turkcell.library_app.enums.BookCopyStatus;
import com.turkcell.library_app.enums.ReservationStatus;
import com.turkcell.library_app.repository.BookCopyRepository;
import com.turkcell.library_app.repository.BookRepository;
import com.turkcell.library_app.repository.ReservationRepository;
import com.turkcell.library_app.repository.StudentRepository;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final StudentRepository studentRepository;
    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    public ReservationServiceImpl(
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
    public ReservationResponse create(CreateReservationRequest request) {
        if (request.getStudentId() == null || request.getBookId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "studentId and bookId are required");
        }

        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found"));
        if (!Boolean.TRUE.equals(student.getIsActive())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student is not active");
        }

        Book book = bookRepository.findById(request.getBookId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book not found"));
        if (book.getDeletedAt() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book is deleted");
        }

        long availableCount = bookCopyRepository.countByBookIdAndStatus(book.getId(), BookCopyStatus.AVAILABLE);
        if (availableCount > 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book has available copies, reservation is not needed");
        }

        Reservation reservation = new Reservation();
        reservation.setStudent(student);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setExpiresAt(request.getExpiresAt() == null ? LocalDateTime.now().plusDays(3) : request.getExpiresAt());
        long queueSize = reservationRepository.countByBookIdAndStatus(book.getId(), ReservationStatus.PENDING);
        reservation.setPosition((int) queueSize + 1);

        return map(reservationRepository.save(reservation));
    }

    @Override
    public ReservationResponse getById(UUID id) {
        return map(findReservation(id));
    }

    @Override
    public Page<ReservationResponse> getAll(UUID bookId, ReservationStatus status, int page, int size) {
        if (bookId != null && status != null) {
            return reservationRepository.findByBookIdAndStatus(bookId, status, PageRequest.of(page, size)).map(this::map);
        }
        if (bookId != null) {
            return reservationRepository.findByBookId(bookId, PageRequest.of(page, size)).map(this::map);
        }
        if (status != null) {
            return reservationRepository.findByStatus(status, PageRequest.of(page, size)).map(this::map);
        }
        return reservationRepository.findAll(PageRequest.of(page, size)).map(this::map);
    }

    @Override
    public ReservationResponse updateStatus(UUID id, UpdateReservationStatusRequest request) {
        if (request.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
        }
        Reservation reservation = findReservation(id);
        reservation.setStatus(request.getStatus());
        if (request.getExpiresAt() != null) {
            reservation.setExpiresAt(request.getExpiresAt());
        }
        return map(reservationRepository.save(reservation));
    }

    @Override
    public void delete(UUID id) {
        Reservation reservation = findReservation(id);
        reservationRepository.delete(reservation);
    }

    private Reservation findReservation(UUID id) {
        return reservationRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));
    }

    private ReservationResponse map(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setStudentId(reservation.getStudent() == null ? null : reservation.getStudent().getId());
        response.setBookId(reservation.getBook() == null ? null : reservation.getBook().getId());
        response.setStatus(reservation.getStatus());
        response.setPosition(reservation.getPosition());
        response.setReservedAt(reservation.getReservedAt());
        response.setExpiresAt(reservation.getExpiresAt());
        return response;
    }
}
