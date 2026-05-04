package com.turkcell.library_cqrs.application.features.staff.query.getall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.staff.StaffResponse;
import com.turkcell.library_cqrs.application.features.staff.mapper.StaffMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.StaffRepository;

@Component
public class GetStaffsQueryHandler implements QueryHandler<GetStaffsQuery, Page<StaffResponse>> {

    private final StaffRepository repository;

    public GetStaffsQueryHandler(StaffRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<StaffResponse> handle(GetStaffsQuery query) {
        return repository.findAll(PageRequest.of(query.page(), query.size())).map(StaffMapper::toDto);
    }
}
