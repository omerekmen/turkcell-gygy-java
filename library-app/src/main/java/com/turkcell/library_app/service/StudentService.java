package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.turkcell.library_app.dto.student.CreateStudentRequest;
import com.turkcell.library_app.dto.student.StudentResponse;
import com.turkcell.library_app.dto.student.UpdateStudentRequest;

public interface StudentService {
    StudentResponse create(CreateStudentRequest request);
    StudentResponse getById(UUID id);
    Page<StudentResponse> getAll(int page, int size);
    StudentResponse update(UUID id, UpdateStudentRequest request);
    void delete(UUID id);
}
