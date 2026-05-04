package com.turkcell.library_cqrs.application.features.reservation.query.getall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.turkcell.library_cqrs.api.dto.reservation.ReservationResponse;
import com.turkcell.library_cqrs.application.features.reservation.mapper.ReservationMapper;
import com.turkcell.library_cqrs.core.mediator.cqrs.QueryHandler;
import com.turkcell.library_cqrs.domain.enums.ReservationStatus;
import com.turkcell.library_cqrs.infrastructure.persistance.postgresql.ReservationRepository;

@Component
public class GetReservationsQueryHandler implements QueryHandler<GetReservationsQuery, Page<ReservationResponse>> {

    private final ReservationRepository reservationRepository;

    public GetReservationsQueryHandler(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Page<ReservationResponse> handle(GetReservationsQuery query) {
        var pageable = PageRequest.of(query.page(), query.size());

        if (query.bookId() != null && query.status() != null) {
            return reservationRepository.findByBookIdAndStatus(query.bookId(), query.status(), pageable)
                .map(ReservationMapper::toDto);
        }
        if (query.bookId() != null) {
            return reservationRepository.findByBookId(query.bookId(), pageable).map(ReservationMapper::toDto);
        }
        if (query.status() != null) {
            return reservationRepository.findByStatus(query.status(), pageable).map(ReservationMapper::toDto);
        }

        return reservationRepository.findAll(pageable).map(ReservationMapper::toDto);
    }
}
