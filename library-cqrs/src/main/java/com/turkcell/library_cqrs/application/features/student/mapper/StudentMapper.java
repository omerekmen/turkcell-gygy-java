package com.turkcell.library_cqrs.application.features.student.mapper;

import java.util.List;

import com.turkcell.library_cqrs.application.features.student.StudentResponse;
import com.turkcell.library_cqrs.domain.entity.Student;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static StudentResponse toDto(Student entity) {
        if (entity == null) {
            return null;
        }

        return new StudentResponse(
            entity.getId(),
            entity.getUser() != null ? entity.getUser().getId() : null,
            entity.getStudentNumber(),
            entity.getIsActive()
        );
    }

    public static List<StudentResponse> toDtoList(List<Student> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(StudentMapper::toDto).toList();
    }
}
