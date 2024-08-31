package com.booking.notificationservice.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.booking.notificationservice.dto.response.AppNotificationResponse;
import com.booking.notificationservice.dto.response.PageResponse;
import com.booking.notificationservice.mapper.AppNotificationMapper;
import com.booking.notificationservice.repository.AppNotificationRepository;
import com.booking.notificationservice.utils.TokenUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppNotificationService {
    AppNotificationRepository appNotificationRepository;
    AppNotificationMapper appNotificationMapper;
    TokenUtil tokenUtTokenUtil;

    public PageResponse<AppNotificationResponse> getMyNotification(int page, int size){
        String userId = tokenUtTokenUtil.getUserIdFromToken();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = appNotificationRepository.findAllByUserId(userId, pageable);

        return PageResponse.<AppNotificationResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream().map(appNotificationMapper::toAppNotificationResponse).toList())
                .build();
    }
}
