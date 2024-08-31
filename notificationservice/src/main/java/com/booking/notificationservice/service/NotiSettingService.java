package com.booking.notificationservice.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.booking.notificationservice.dto.request.UpdateNotiSettingRequest;
import com.booking.notificationservice.dto.response.NotiSettingResponse;
import com.booking.notificationservice.entity.NotificationSetting;
import com.booking.notificationservice.exception.AppException;
import com.booking.notificationservice.exception.ErrorCode;
import com.booking.notificationservice.mapper.NotiSettingMapper;
import com.booking.notificationservice.repository.NotificationSettingRepository;
import com.booking.notificationservice.utils.TokenUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotiSettingService {
    NotiSettingMapper notiSettingMapper;
    NotificationSettingRepository notiSettingRepository;

    TokenUtil tokenUtil;

    public NotiSettingResponse getMySetting(){
        String userId = tokenUtil.getUserIdFromToken();
     
        Optional<NotificationSetting> settingOptional = notiSettingRepository.findFirstByUserId(userId);
        NotificationSetting setting;

        if(!settingOptional.isPresent()){
            setting = NotificationSetting.builder().build();
            setting = notiSettingRepository.save(setting);
        }else{
            setting = settingOptional.get();
        }

        return notiSettingMapper.toNotiSettingResponse(setting);
    }

    public NotiSettingResponse updateMySetting(UpdateNotiSettingRequest request){
        String userId = tokenUtil.getUserIdFromToken();

        NotificationSetting setting = notiSettingRepository.findFirstByUserId(userId)
                    .orElseThrow(()-> new AppException(ErrorCode.SETTING_NOT_FOUND));
        
        setting = notiSettingMapper.toNotificationSetting(request);

        return notiSettingMapper.toNotiSettingResponse(notiSettingRepository.save(setting));
    }
}
