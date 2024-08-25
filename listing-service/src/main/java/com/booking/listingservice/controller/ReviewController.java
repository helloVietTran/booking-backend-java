package com.booking.listingservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.booking.listingservice.dto.response.PageResponse;
import com.booking.listingservice.dto.response.ReviewResponse;
import com.booking.listingservice.service.ReviewService;
import com.booking.listingservice.dto.ApiResponse;
import com.booking.listingservice.dto.request.ReviewCreationRequest;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {
    ReviewService reviewService;

    @PostMapping
    ApiResponse<ReviewResponse> createReview(@RequestBody @Valid ReviewCreationRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.createReview(request))
                .build();
    }

    @GetMapping("/{reviewId}")
    ApiResponse<ReviewResponse> getReview(@PathVariable String reviewId) {
        return ApiResponse.<ReviewResponse>builder()
                .result(reviewService.getReview(reviewId))
                .build();
    }

    @GetMapping("/{listingId}/all-posts")
    ApiResponse<PageResponse<ReviewResponse>> getReviewPosts(
            @PathVariable String listingId,
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "size", required = true, defaultValue = "10") int size)

    {
        return ApiResponse.<PageResponse<ReviewResponse>>builder()
                .result(reviewService.getReviewPosts(listingId, page, size))
                .build();
    }

    @GetMapping("/my-review-post")
    ApiResponse<PageResponse<ReviewResponse>> getMyReviewPosts(
            @RequestParam(value = "page", required = true, defaultValue = "1") int page,
            @RequestParam(value = "size", required = true, defaultValue = "10") int size) {

        return ApiResponse
                .<PageResponse<ReviewResponse>>builder()
                .result(reviewService.getMyReviewPosts(page, size))
                .build();
    }

}
