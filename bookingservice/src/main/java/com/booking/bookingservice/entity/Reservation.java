package com.booking.bookingservice.entity;


import java.time.Instant;

import com.booking.bookingservice.enums.PaymentStatus;
import com.booking.bookingservice.enums.ReservationStatus;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String renterId;// id người thuê nhà, được xác định qua token
    String listingId;// id nhà
    
    Instant checkInDate;
    Instant checkOutDate;

    Integer adultCount;
    Integer childrenCount;
    Double totalPrice;
    
    @Builder.Default
    ReservationStatus status = ReservationStatus.PENDING;

    @Builder.Default
    PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    Instant createdAt;
    Instant updatedAt;

    // trường này để đánh dấu người dùng đã hoàn thành quá trình thuê phòng
    // được gọi từ review service, chỉ cho phép review khi người dùng đó thuê xong
    @Builder.Default
    boolean hasCheckout = false;

    Instant checkoutAt;

    @PrePersist
    public void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate 
    public void onUpdate(){
        updatedAt = Instant.now();
    }
}
