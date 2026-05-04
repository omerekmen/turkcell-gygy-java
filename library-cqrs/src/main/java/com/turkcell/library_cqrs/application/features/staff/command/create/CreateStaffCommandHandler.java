package com.turkcell.library_cqrs.application.features.staff.command.create;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.application.features.staff.mapper.StaffMapper;
import com.turkcell.library_cqrs.domain.entity.Staff;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StaffRepository;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.UserRepository;

@Component
public class CreateStaffCommandHandler implements CommandHandler<CreateStaffCommand, StaffResponse> {

    private final StaffRepository staffRepository;
    private final UserRepository userRepository;

    public CreateStaffCommandHandler(StaffRepository staffRepository, UserRepository userRepository) {
        this.staffRepository = staffRepository;
        this.userRepository = userRepository;
    }

    @Override
    public StaffResponse handle(CreateStaffCommand command) {
        if (command.userId() == null || command.staffNumber() == null || command.staffNumber().isBlank() || command.role() == null) {
            throw new IllegalArgumentException("userId, staffNumber and role are required");
        }

        var staffNumber = command.staffNumber().trim();
        if (staffRepository.existsByStaffNumber(staffNumber)) {
            throw new IllegalArgumentException("Staff number already exists");
        }
        if (staffRepository.existsByUserId(command.userId())) {
            throw new IllegalArgumentException("User already linked to staff");
        }

        var user = userRepository.findById(command.userId())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var staff = new Staff();
        staff.setUser(user);
        staff.setStaffNumber(staffNumber);
        staff.setRole(command.role());
        staff.setIsActive(command.isActive() == null ? Boolean.TRUE : command.isActive());

        return StaffMapper.toDto(staffRepository.save(staff));
    }
}
