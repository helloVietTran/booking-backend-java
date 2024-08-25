package com.booking.bookingservice.entity;


import java.time.LocalDateTime;

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

    String guestName;
    String phoneNumber;
    
    LocalDateTime checkInDate;
    LocalDateTime checkOutDate;
    Integer adultCount;
    Integer childrenCount;
    String totalPrice;
    
    @Builder.Default
    ReservationStatus status = ReservationStatus.PENDING;// mặc định là đang chờ xác nhận

    @Builder.Default
    PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    // trường này để đánh dấu người dùng đã hoàn thành quá trình thuê phòng
    // được gọi từ review service, chỉ cho phép review khi người dùng đó thuê xong
    @Builder.Default
    boolean hasCheckout = false;

    LocalDateTime checkoutAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate 
    public void onUpdate(){
        updatedAt = LocalDateTime.now();
    }
}
