package com.turkcell.library_cqrs.application.features.staff.command.update;

import java.util.UUID;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.core.mediator.cqrs.Command;
import com.turkcell.library_cqrs.domain.enums.StaffRole;

public record UpdateStaffCommand(UUID id, String staffNumber, StaffRole role, Boolean isActive)
    implements Command<StaffResponse> {
}
