package com.booking.notificationservice.mapper;

import org.mapstruct.Mapper;

import com.booking.notificationservice.dto.response.AppNotificationResponse;
import com.booking.notificationservice.entity.AppNotification;

@Mapper(componentModel = "spring")
public interface AppNotificationMapper {
    AppNotificationResponse toAppNotificationResponse(AppNotification appNotification);
}
