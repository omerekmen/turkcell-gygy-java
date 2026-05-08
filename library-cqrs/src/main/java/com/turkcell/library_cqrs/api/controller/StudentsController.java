package com.turkcell.library_cqrs.api.controller;

import java.net.URI;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkcell.library_cqrs.api.dto.ApiResult;
import com.turkcell.library_cqrs.application.features.student.StudentResponse;
import com.turkcell.library_cqrs.application.features.student.command.create.CreateStudentCommand;
import com.turkcell.library_cqrs.application.features.student.command.delete.DeleteStudentCommand;
import com.turkcell.library_cqrs.application.features.student.command.update.UpdateStudentCommand;
import com.turkcell.library_cqrs.application.features.student.query.getall.GetStudentsQuery;
import com.turkcell.library_cqrs.application.features.student.query.getbyid.GetStudentByIdQuery;
import com.turkcell.library_cqrs.core.mediator.Mediator;

@RestController
@RequestMapping("api/v{version:1}/students")
public class StudentsController {

    private final Mediator mediator;

    public StudentsController(Mediator mediator) {
        this.mediator = mediator;
    }

    @PostMapping
    public ResponseEntity<ApiResult<StudentResponse>> create(@Valid @RequestBody CreateStudentCommand request) {
        var dto = mediator.send(request);

        return ResponseEntity.created(URI.create("/students/" + dto.id()))
            .body(ApiResult.success(HttpStatus.CREATED.value(), "Student created successfully", dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<StudentResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResult.success("Student retrieved successfully", mediator.send(new GetStudentByIdQuery(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResult<Page<StudentResponse>>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(ApiResult.success("Students retrieved successfully", mediator.send(new GetStudentsQuery(page, size))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResult<StudentResponse>> update(@PathVariable UUID id, @Valid @RequestBody UpdateStudentCommand request) {
        if (!id.equals(request.id())) {
            throw new IllegalArgumentException("Path id and request body id must match");
        }

        return ResponseEntity.ok(ApiResult.success("Student updated successfully", mediator.send(request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable UUID id) {
        mediator.send(new DeleteStudentCommand(id));
        return ResponseEntity.ok(ApiResult.success(HttpStatus.NO_CONTENT.value(), "Student deleted successfully"));
    }
}
