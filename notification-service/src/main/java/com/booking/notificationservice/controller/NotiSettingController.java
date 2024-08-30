package com.booking.notificationservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.notificationservice.dto.ApiResponse;
import com.booking.notificationservice.dto.request.UpdateNotiSettingRequest;
import com.booking.notificationservice.dto.response.NotiSettingResponse;
import com.booking.notificationservice.service.NotiSettingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/setting")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotiSettingController {
    NotiSettingService notiSettingService;


    @GetMapping("/my-setting")
    ApiResponse<NotiSettingResponse> getMySetting(){
        return ApiResponse.<NotiSettingResponse>builder()
                    .result(notiSettingService.getMySetting())
                    .build();
    }

    @PutMapping("/my-setting")
    ApiResponse<NotiSettingResponse> updateMySetting(@RequestBody UpdateNotiSettingRequest request){
        return ApiResponse.<NotiSettingResponse>builder()
        .result(notiSettingService.updateMySetting(request))
        .build();
    }
}
