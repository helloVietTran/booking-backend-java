package com.booking.listingservice.controller;

import java.util.List;

import org.bson.Document;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.listingservice.dto.ApiResponse;
import com.booking.listingservice.dto.request.ListingCreationRequest;
import com.booking.listingservice.dto.request.UpdateListingImageRequest;
import com.booking.listingservice.dto.response.ListingResponse;
import com.booking.listingservice.dto.response.PageResponse;
import com.booking.listingservice.service.ListingService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingController {

        ListingService listingService;

        @PostMapping("/new-listing")
        ApiResponse<ListingResponse> createListing(@RequestBody @Valid ListingCreationRequest request) {
                return ApiResponse.<ListingResponse>builder()
                                .result(listingService.createListing(request))
                                .build();
        }

        @DeleteMapping("/my/delete/{listingId}")
        ApiResponse<String> deleteMyListing(@PathVariable String listingId) {
                listingService.deleteMyListing(listingId);
                return ApiResponse.<String>builder()
                                .result("Your listing has been deleted")
                                .build();
        }

        @GetMapping("/{listingId}")
        ApiResponse<ListingResponse> getListing(@PathVariable String listingId) {
                return ApiResponse.<ListingResponse>builder()
                                .result(listingService.getListing(listingId))
                                .build();
        }

        // call from file service
        @PatchMapping("/my/listing/image")
        ApiResponse<String> updateListingImages(@RequestBody UpdateListingImageRequest request) {
                listingService.updateListingImages(request);

                return ApiResponse.<String>builder()
                                .result("Upload listing images successfull")
                                .build();
        }

        @GetMapping("/10-listing")
        ApiResponse<PageResponse<ListingResponse>> getListings(
                        @RequestParam(value = "page", required = true, defaultValue = "1") int page,
                        @RequestParam(value = "size", required = true, defaultValue = "10") int size) {
                return ApiResponse.<PageResponse<ListingResponse>>builder()
                                .result(listingService.getListings(page, size))
                                .build();
        }

        // lấy danh sách listing của bản thân
        @GetMapping("/my-listing")
        ApiResponse<PageResponse<ListingResponse>> getMyListings(
                        @RequestParam(value = "page", required = true, defaultValue = "1") int page,
                        @RequestParam(value = "size", required = true, defaultValue = "10") int size) {

                return ApiResponse
                                .<PageResponse<ListingResponse>>builder()
                                .result(listingService.getMyListings(page, size))
                                .build();
        }

        @GetMapping("/top-10")
        ApiResponse<List<Document>> findTop10HighestRated() {
                return ApiResponse.<List<Document>>builder()
                                .result(listingService.findTop10HighestRated())
                                .build();
        }

}
