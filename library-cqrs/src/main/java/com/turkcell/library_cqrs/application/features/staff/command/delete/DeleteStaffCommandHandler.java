package com.turkcell.library_cqrs.application.features.staff.command.delete;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.core.mediator.cqrs.CommandHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StaffRepository;

@Component
public class DeleteStaffCommandHandler implements CommandHandler<DeleteStaffCommand, Void> {

    private final StaffRepository staffRepository;

    public DeleteStaffCommandHandler(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Void handle(DeleteStaffCommand command) {
        var staff = staffRepository.findById(command.id())
            .orElseThrow(() -> new IllegalArgumentException("Staff not found"));

        staffRepository.delete(staff);
        return null;
    }
}
