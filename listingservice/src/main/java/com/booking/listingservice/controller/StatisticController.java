package com.booking.listingservice.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.listingservice.dto.ApiResponse;
import com.booking.listingservice.service.StatisticService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/statistic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticController {
    StatisticService statisticService;

    @PostMapping("/by-region")
    ApiResponse<String> statisticByRegion(){
        statisticService.statisticByRegion();
        return ApiResponse.<String>builder()
            .result("Statistic by region successfull")
            .build();
    }


}
