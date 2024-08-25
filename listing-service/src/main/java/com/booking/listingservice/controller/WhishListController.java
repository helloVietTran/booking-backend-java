package com.booking.listingservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.listingservice.dto.ApiResponse;
import com.booking.listingservice.dto.request.AddToWhishListRequest;
import com.booking.listingservice.dto.request.RemoveFromWhishListRequest;
import com.booking.listingservice.dto.response.WhishListResponse;
import com.booking.listingservice.service.WhishListService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/whish-list")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WhishListController {
    WhishListService whishListService;

    @GetMapping
    ApiResponse<WhishListResponse> getWhishList(){
        return ApiResponse.<WhishListResponse>builder()
                     .result(whishListService.getWhishList())
                    .build();
    }

    @PostMapping("/add")
    ApiResponse<WhishListResponse> addToWhishList(@RequestBody AddToWhishListRequest request){
        return ApiResponse.<WhishListResponse>builder()
                     .result(whishListService.addToWhishList(request))
                    .build();
    }

    @PatchMapping("/remove")
    ApiResponse<WhishListResponse> removeFromWhishList(@RequestBody RemoveFromWhishListRequest request){
        return ApiResponse.<WhishListResponse>builder()
                     .result(whishListService.removeFromWhishList(request))
                    .build();
    }
}
