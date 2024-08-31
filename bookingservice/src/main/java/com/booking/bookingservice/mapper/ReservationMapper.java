package com.booking.bookingservice.mapper;


import org.mapstruct.Mapper;

import com.booking.bookingservice.dto.request.ReservationRequest;
import com.booking.bookingservice.dto.response.ReservationResponse;
import com.booking.bookingservice.entity.Reservation;


@Mapper(componentModel = "spring")
public interface  ReservationMapper {
    Reservation toReservation(ReservationRequest request);

    ReservationResponse toReservationResponse(Reservation Reservation);
}
