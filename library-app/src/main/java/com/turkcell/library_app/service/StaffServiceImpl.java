package com.turkcell.library_app.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.turkcell.library_app.dto.staff.CreateStaffRequest;
import com.turkcell.library_app.dto.staff.StaffResponse;
import com.turkcell.library_app.dto.staff.UpdateStaffRequest;
import com.turkcell.library_app.entity.Staff;
import com.turkcell.library_app.entity.User;
import com.turkcell.library_app.repository.StaffRepository;
import com.turkcell.library_app.repository.UserRepository;

@Service
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    public StaffServiceImpl(StaffRepository staffRepository, UserRepository userRepository) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StaffResponse create(CreateStaffRequest request) {
        if (request.getUserId() == null || request.getStaffNumber() == null || request.getStaffNumber().isBlank() || request.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId, staffNumber and role are required");
        }
        if (staffRepository.existsByStaffNumber(request.getStaffNumber())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Staff number already exists");
        }
        if (staffRepository.existsByUserId(request.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already linked to staff");
        }

        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        Staff staff = new Staff();
        staff.setUser(user);
        staff.setStaffNumber(request.getStaffNumber().trim());
        staff.setRole(request.getRole());
        staff.setIsActive(request.getIsActive() == null ? Boolean.TRUE : request.getIsActive());
        return map(staffRepository.save(staff));
    }

    @Override
    public StaffResponse getById(UUID id) {
        return map(findStaff(id));
    }

    @Override
    public Page<StaffResponse> getAll(int page, int size) {
        return staffRepository.findAll(PageRequest.of(page, size)).map(this::map);
    }

    @Override
    public StaffResponse update(UUID id, UpdateStaffRequest request) {
        Staff staff = findStaff(id);
        if (request.getStaffNumber() != null && !request.getStaffNumber().isBlank()) {
            if (!staff.getStaffNumber().equals(request.getStaffNumber())
                && staffRepository.existsByStaffNumber(request.getStaffNumber())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Staff number already exists");
            }
            staff.setStaffNumber(request.getStaffNumber().trim());
        }
        if (request.getRole() != null) {
            staff.setRole(request.getRole());
        }
        if (request.getIsActive() != null) {
            staff.setIsActive(request.getIsActive());
        }
        return map(staffRepository.save(staff));
    }

    @Override
    public void delete(UUID id) {
        Staff staff = findStaff(id);
        staffRepository.delete(staff);
    }

    private Staff findStaff(UUID id) {
        return staffRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found"));
    }

    private StaffResponse map(Staff staff) {
        StaffResponse response = new StaffResponse();
        response.setId(staff.getId());
        response.setUserId(staff.getUser() == null ? null : staff.getUser().getId());
        response.setStaffNumber(staff.getStaffNumber());
        response.setRole(staff.getRole());
        response.setIsActive(staff.getIsActive());
        return response;
    }
}
