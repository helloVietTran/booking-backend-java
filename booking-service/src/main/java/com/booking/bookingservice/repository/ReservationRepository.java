package com.booking.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.bookingservice.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, String>{

}
