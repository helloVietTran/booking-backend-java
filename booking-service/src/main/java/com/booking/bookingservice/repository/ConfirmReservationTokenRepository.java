package com.booking.bookingservice.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.bookingservice.entity.ConfirmReservationToken;

public interface ConfirmReservationTokenRepository extends JpaRepository<ConfirmReservationToken, Long>{
    Optional<ConfirmReservationToken> findFirstByReservationIdAndExpiryDateAfter(String reservationId, LocalDateTime currentTime);
}
