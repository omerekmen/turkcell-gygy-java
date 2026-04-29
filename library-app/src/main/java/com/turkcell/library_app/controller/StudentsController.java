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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_app.dto.ApiResult;

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
    public ApiResult<StudentResponse> create(@RequestBody CreateStudentRequest request) {
        StudentResponse response = studentService.create(request);
        return ApiResult.success(HttpStatus.CREATED.value(), "Student created successfully", response);
    }

    @GetMapping("/{id}")
    public ApiResult<StudentResponse> getById(@PathVariable UUID id) {
        StudentResponse response = studentService.getById(id);
        return ApiResult.success("Student retrieved successfully", response);
    }

    @GetMapping
    public ApiResult<Page<StudentResponse>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        Page<StudentResponse> response = studentService.getAll(page, size);
        return ApiResult.success("Students retrieved successfully", response);
    }

    @PutMapping("/{id}")
    public ApiResult<StudentResponse> update(@PathVariable UUID id, @RequestBody UpdateStudentRequest request) {
        StudentResponse response = studentService.update(id, request);
        return ApiResult.success("Student updated successfully", response);
        
    }
    
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable UUID id) {
        studentService.delete(id);
        return ApiResult.success(HttpStatus.NO_CONTENT.value(), "Student deleted successfully");
    }
}
