package com.booking.notificationservice.mapper;

import org.mapstruct.Mapper;

import com.booking.notificationservice.dto.request.UpdateNotiSettingRequest;
import com.booking.notificationservice.dto.response.NotiSettingResponse;
import com.booking.notificationservice.entity.NotificationSetting;

@Mapper(componentModel = "spring")
public interface NotiSettingMapper {
    NotiSettingResponse toNotiSettingResponse(NotificationSetting setting);
    
    NotificationSetting toNotificationSetting(UpdateNotiSettingRequest request);
}
