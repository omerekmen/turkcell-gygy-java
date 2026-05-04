package com.turkcell.library_cqrs.application.features.staff.query.getbyid;

import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.application.features.staff.mapper.StaffMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StaffRepository;

@Component
public class GetStaffByIdQueryHandler implements QueryHandler<GetStaffByIdQuery, StaffResponse> {

    private final StaffRepository repository;

    public GetStaffByIdQueryHandler(StaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public StaffResponse handle(GetStaffByIdQuery query) {
        return StaffMapper.toDto(repository.findById(query.id())
            .orElseThrow(() -> new IllegalArgumentException("Staff not found")));
    }
}
