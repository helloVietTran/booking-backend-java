package com.booking.listingservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.listingservice.dto.ApiResponse;
import com.booking.listingservice.dto.request.RegionCreationRequest;
import com.booking.listingservice.dto.response.RegionResponse;
import com.booking.listingservice.service.RegionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegionController {
    RegionService regionService;

    @PostMapping
    ApiResponse<RegionResponse> createRegion(@RequestBody RegionCreationRequest request) {

        return ApiResponse.<RegionResponse>builder()
                .result(regionService.createRegion(request))
                .build();
    }

    // ?label
  /*   @GetMapping
    ApiResponse<List<RegionResponse>> getRegions(@RequestParam String label) {
        return ApiResponse.<List<RegionResponse>>builder()
                .result(regionService.getRegions(label))
                .build();
    }

    */
    @GetMapping("/get-all")
    ApiResponse<List<RegionResponse>> getStatisticListByRegion(){ 
        return ApiResponse.<List<RegionResponse>>builder()
            .result(regionService.getAllRegion())
            .build();
    }
}
