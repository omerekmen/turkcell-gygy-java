package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.library_app.dto.student.CreateStudentRequest;
import com.turkcell.library_app.dto.student.StudentResponse;
import com.turkcell.library_app.dto.student.UpdateStudentRequest;
import com.turkcell.library_app.entity.Student;
import com.turkcell.library_app.entity.User;
import com.turkcell.library_app.repository.StudentRepository;
import com.turkcell.library_app.repository.UserRepository;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    public StudentServiceImpl(StudentRepository studentRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StudentResponse create(CreateStudentRequest request) {
        if (request.getUserId() == null || request.getStudentNumber() == null || request.getStudentNumber().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId and studentNumber are required");
        }
        if (studentRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student number already exists");
        }
        if (studentRepository.existsByUserId(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already linked to a student");
        }

        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        Student student = new Student();
        student.setUser(user);
        student.setStudentNumber(request.getStudentNumber().trim());
        student.setIsActive(request.getIsActive() == null ? Boolean.TRUE : request.getIsActive());

        return map(studentRepository.save(student));
    }

    @Override
    public StudentResponse getById(UUID id) {
        return map(findStudent(id));
    }

    @Override
    public Page<StudentResponse> getAll(int page, int size) {
        return studentRepository.findAll(PageRequest.of(page, size)).map(this::map);
    }

    @Override
    public StudentResponse update(UUID id, UpdateStudentRequest request) {
        Student student = findStudent(id);
        if (request.getStudentNumber() != null && !request.getStudentNumber().isBlank()) {
            if (!student.getStudentNumber().equals(request.getStudentNumber())
                && studentRepository.existsByStudentNumber(request.getStudentNumber())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Student number already exists");
            }
            student.setStudentNumber(request.getStudentNumber().trim());
        }
        if (request.getIsActive() != null) {
            student.setIsActive(request.getIsActive());
        }
        return map(studentRepository.save(student));
    }

    @Override
    public void delete(UUID id) {
        Student student = findStudent(id);
        studentRepository.delete(student);
    }

    private Student findStudent(UUID id) {
        return studentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
    }

    private StudentResponse map(Student student) {
        StudentResponse response = new StudentResponse();
        response.setId(student.getId());
        response.setUserId(student.getUser() == null ? null : student.getUser().getId());
        response.setStudentNumber(student.getStudentNumber());
        response.setIsActive(student.getIsActive());
        return response;
    }
}
