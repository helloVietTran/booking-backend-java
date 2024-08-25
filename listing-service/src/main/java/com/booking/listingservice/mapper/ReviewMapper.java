package com.booking.listingservice.mapper;

import org.mapstruct.Mapper;

import com.booking.listingservice.dto.response.ReviewResponse;
import com.booking.listingservice.dto.request.ReviewCreationRequest;
import com.booking.listingservice.entity.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(ReviewCreationRequest request);
    ReviewResponse toReviewResponse(Review review);
}
