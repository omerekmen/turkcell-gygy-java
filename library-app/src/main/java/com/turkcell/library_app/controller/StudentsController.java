package com.turkcell.library_app.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_app.dto.student.CreateStudentRequest;
import com.turkcell.library_app.dto.student.StudentResponse;
import com.turkcell.library_app.dto.student.UpdateStudentRequest;
import com.turkcell.library_app.service.StudentService;

@RestController
@RequestMapping("/api/v{version:1}/students")
public class StudentsController {
    private final StudentService studentService;

    public StudentsController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public StudentResponse create(@RequestBody CreateStudentRequest request) {
        return studentService.create(request);
    }

    @GetMapping("/{id}")
    public StudentResponse getById(@PathVariable UUID id) {
        return studentService.getById(id);
    }

    @GetMapping
    public Page<StudentResponse> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return studentService.getAll(page, size);
    }

    @PutMapping("/{id}")
    public StudentResponse update(@PathVariable UUID id, @RequestBody UpdateStudentRequest request) {
        return studentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        studentService.delete(id);
    }
}
