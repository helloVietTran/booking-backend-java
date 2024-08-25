package com.booking.listingservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.booking.listingservice.dto.ApiResponse;
import com.booking.listingservice.dto.request.ListingTypeCreationRequest;
import com.booking.listingservice.dto.response.ListingTypeResponse;
import com.booking.listingservice.service.ListingTypeService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/listing-type")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingTypeController {
    ListingTypeService listingTypeService;

    @PostMapping
    ApiResponse<ListingTypeResponse> create(@RequestBody ListingTypeCreationRequest request){
        return ApiResponse.<ListingTypeResponse>builder()
                          .result(listingTypeService.create(request))
                          .build();
    }

    @DeleteMapping("/{listingTypeId}")
    ApiResponse<Void> delete(@PathVariable String listingTypeId){
        listingTypeService.delete(listingTypeId);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    ApiResponse<List<ListingTypeResponse>> getAll(){
        return ApiResponse.<List<ListingTypeResponse>>builder()
                          .result(listingTypeService.getAll())
                          .build();
    }
}
