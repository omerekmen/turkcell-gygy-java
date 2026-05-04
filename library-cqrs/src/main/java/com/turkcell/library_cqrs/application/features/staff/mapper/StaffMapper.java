package com.turkcell.library_cqrs.application.features.staff.mapper;

import java.util.List;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.domain.entity.Staff;

public final class StaffMapper {

    private StaffMapper() {
    }

    public static StaffResponse toDto(Staff entity) {
        if (entity == null) {
            return null;
        }

        var dto = new StaffResponse();
        dto.setId(entity.getId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setStaffNumber(entity.getStaffNumber());
        dto.setRole(entity.getRole());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public static List<StaffResponse> toDtoList(List<Staff> entities) {
        if (entities == null) {
            return List.of();
        }

        return entities.stream().map(StaffMapper::toDto).toList();
    }
}
