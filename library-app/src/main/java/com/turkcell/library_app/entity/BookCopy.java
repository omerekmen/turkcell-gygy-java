package com.turkcell.library_app.entity;

import java.util.UUID;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;

import com.turkcell.library_app.enums.BookCopyStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_copies")
public class BookCopy {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(name = "barcode", nullable = false, unique = true, length = 100)
    private String barcode;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelve shelve;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private BookCopyStatus status;

    @ManyToMany(mappedBy = "bookCopies")
    private List<Loan> loans;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Shelve getShelve() {
        return shelve;
    }

    public void setShelve(Shelve shelve) {
        this.shelve = shelve;
    }

    public BookCopyStatus getStatus() {
        return status;
    }

    public void setStatus(BookCopyStatus status) {
        this.status = status;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
