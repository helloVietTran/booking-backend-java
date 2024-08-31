package com.booking.notificationservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.booking.notificationservice.entity.NotificationSetting;

public interface NotificationSettingRepository extends MongoRepository<NotificationSetting, String>{
    Optional<NotificationSetting> findFirstByUserId(String userId);
}
