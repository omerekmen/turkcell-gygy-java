package com.turkcell.library_cqrs.application.features.staff.mapper;

import java.util.List;

import com.turkcell.library_cqrs.application.features.staff.StaffResponse;
import com.turkcell.library_cqrs.domain.entity.Staff;

public final class StaffMapper {

    private StaffMapper() {
    }

    public static StaffResponse toDto(Staff entity) {
        if (entity == null) {
            return null;
        }

        return new StaffResponse(
            entity.getId(),
            entity.getUser() != null ? entity.getUser().getId() : null,
            entity.getStaffNumber(),
            entity.getRole(),
            entity.getIsActive()
        );
    }

    public static List<StaffResponse> toDtoList(List<Staff> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(StaffMapper::toDto).toList();
    }
}
