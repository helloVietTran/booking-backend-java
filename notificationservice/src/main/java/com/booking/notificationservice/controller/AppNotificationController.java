package com.booking.notificationservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.notificationservice.dto.ApiResponse;
import com.booking.notificationservice.dto.response.AppNotificationResponse;
import com.booking.notificationservice.dto.response.PageResponse;
import com.booking.notificationservice.service.AppNotificationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppNotificationController {

    AppNotificationService appNotificationService;

    @GetMapping("/my-notification")
    ApiResponse<PageResponse<AppNotificationResponse>> getMyNotification(
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "size", required = true, defaultValue = "5") int size) {
        return ApiResponse.<PageResponse<AppNotificationResponse>>builder()
                .result(appNotificationService.getMyNotification(page, size))
                .build();
    }
}
