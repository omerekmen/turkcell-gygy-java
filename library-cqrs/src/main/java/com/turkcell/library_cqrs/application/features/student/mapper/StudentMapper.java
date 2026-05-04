package com.turkcell.library_cqrs.application.features.student.mapper;

import java.util.List;

import com.turkcell.library_cqrs.api.dto.student.StudentResponse;
import com.turkcell.library_cqrs.domain.entity.Student;

public final class StudentMapper {

    private StudentMapper() {
    }

    public static StudentResponse toDto(Student entity) {
        if (entity == null) {
            return null;
        }

        var dto = new StudentResponse();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setStudentNumber(entity.getStudentNumber());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public static List<StudentResponse> toDtoList(List<Student> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(StudentMapper::toDto).toList();
    }
}
