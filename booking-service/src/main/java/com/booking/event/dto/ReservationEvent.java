package com.booking.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import com.booking.event.enums.ReservationStatus;

@Data
@NoArgsConstructor
public class ReservationEvent{

    private UUID eventId=UUID.randomUUID();
    private Date eventDate = new Date();

    private ReservationStatus reservationStatus;
    ReservationRequest reservationRequest;

    public ReservationEvent(ReservationRequest reservationRequest, ReservationStatus reservationStatus) {
        this.reservationRequest = reservationRequest;
        this.reservationStatus = reservationStatus;
    }
}
