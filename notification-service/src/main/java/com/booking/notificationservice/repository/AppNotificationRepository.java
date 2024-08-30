package com.booking.notificationservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.booking.notificationservice.entity.AppNotification;

@Repository
public interface AppNotificationRepository extends MongoRepository<AppNotification, String>{
    int countByUserId(String userId);

    Page<AppNotification> findAllByUserId(String userId, Pageable pageable);
}
