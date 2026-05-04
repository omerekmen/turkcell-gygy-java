package com.turkcell.library_cqrs.application.features.staff.command.update;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.application.features.staff.mapper.StaffMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StaffRepository;

@Component
public class UpdateStaffCommandHandler implements CommandHandler<UpdateStaffCommand, StaffResponse> {

    private final StaffRepository staffRepository;

    public UpdateStaffCommandHandler(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public StaffResponse handle(UpdateStaffCommand command) {
        var staff = staffRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        if (command.staffNumber() != null && !command.staffNumber().isBlank()) {
            var staffNumber = command.staffNumber().trim();
            if (!staff.getStaffNumber().equals(staffNumber) && staffRepository.existsByStaffNumber(staffNumber)) {
                throw new IllegalArgumentException("Staff number already exists");
            }
            staff.setStaffNumber(staffNumber);
        }

        if (command.role() != null) {
            staff.setRole(command.role());
        }

        if (command.isActive() != null) {
            staff.setIsActive(command.isActive());
        }

        return StaffMapper.toDto(staffRepository.save(staff));
    }
}
