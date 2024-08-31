package com.booking.listingservice.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.listingservice.dto.ApiResponse;
import com.booking.listingservice.dto.request.RecentSearchCreationRequest;
import com.booking.listingservice.dto.response.ListingResponse;
import com.booking.listingservice.dto.response.RecentSearchResponse;
import com.booking.listingservice.service.SearchService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchController {
        SearchService searchService;

        @GetMapping
        ApiResponse<List<ListingResponse>> searchListing(
                        @RequestParam(required = true) String city,
                        @RequestParam(required = false) LocalDateTime checkin,
                        @RequestParam(required = false) LocalDateTime checkout,
                        @RequestParam(name = "adult_count", required = false) Integer adultCount,
                        @RequestParam(name = "children_count", required = false) Integer childrenCount,
                        @RequestParam(name = "room_count", required = false) Integer roomCount,
                        @RequestParam(name = "min_price", required = false) Double minPrice,
                        @RequestParam(name = "max_price", required = false) Double maxPrice,
                        @RequestParam(required = false) List<String> amenities,
                        @RequestParam(name = "min_rating", required = false) Integer minRating,
                        @RequestParam(name = "bedroom_count", required = false) Integer bedroomCount,
                        @RequestParam(name = "bathroom_count", required = false) Integer bathroomCount,
                        @RequestParam(name = "des_type", required = false) List<String> listingTypes,
                        @RequestParam(name = "mealplan", required = false) Boolean hasBreakfast) {

                return ApiResponse.<List<ListingResponse>>builder()
                                .result(searchService.search(city, checkin, checkout, adultCount,
                                                childrenCount, roomCount, minPrice, maxPrice,
                                                amenities, minRating, bedroomCount,
                                                bathroomCount, listingTypes, hasBreakfast))
                                .build();
        }


        @GetMapping("/recent-search")
        ApiResponse<RecentSearchResponse> getRecentSearch() {
                return ApiResponse.<RecentSearchResponse>builder()
                                .result(searchService.getRecentSearch())
                                .build();
        }

        @PostMapping("/recent-search")
        ApiResponse<RecentSearchResponse> createRecentSearch(@RequestBody RecentSearchCreationRequest request) {
                return ApiResponse.<RecentSearchResponse>builder()
                                .result(searchService.createRecentSearch(request))
                                .build();
        }
  
}
