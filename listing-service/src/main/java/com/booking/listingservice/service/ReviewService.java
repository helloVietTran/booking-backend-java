package com.booking.listingservice.service;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.booking.listingservice.dto.response.PageResponse;
import com.booking.listingservice.dto.response.ReservationResponse;
import com.booking.listingservice.dto.response.ReviewResponse;
import com.booking.listingservice.enums.PaymentStatus;
import com.booking.listingservice.repository.ListingRepository;
import com.booking.listingservice.repository.ReviewRepository;
import com.booking.listingservice.repository.httpclient.ReservationClient;
import com.booking.listingservice.dto.request.ReviewCreationRequest;
import com.booking.listingservice.entity.Listing;
import com.booking.listingservice.entity.Review;
import com.booking.listingservice.exception.AppException;
import com.booking.listingservice.exception.ErrorCode;
import com.booking.listingservice.mapper.ReviewMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {

    ReviewRepository reviewRepository;
    ListingRepository listingRepository;

    ReviewMapper reviewMapper;

    ReservationClient reservationClient;
    TokenService tokenService;

    public ReviewResponse createReview(ReviewCreationRequest request) {
        String userId = tokenService.getUserIdFromToken();

        // gọi tới api xem có cho phép review khoong
       /*  ReservationResponse reservation = reservationClient.getReservation(request.getListingId(), userId);

        if (!(reservation.getPaymentStatus() == PaymentStatus.PAID && reservation.getHasCheckout())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }*/

        Review review = reviewMapper.toReview(request);
        review.setUserId(userId);
        reviewRepository.save(review);

        updateRating(request.getListingId(), review);

        return reviewMapper.toReviewResponse(review);
    }

    public ReviewResponse getReview(String id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if (!optionalReview.isPresent()) {
            throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
        }

        return reviewMapper.toReviewResponse(optionalReview.get());
    }

    public PageResponse<ReviewResponse> getReviewPosts(String listingId, int page, int size) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = reviewRepository.findAllByListingId(listingId, pageable);

        return PageResponse.<ReviewResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream().map(reviewMapper::toReviewResponse).toList())
                .build();
    }

    public PageResponse<ReviewResponse> getMyReviewPosts(int page, int size) {
        String userId = tokenService.getUserIdFromToken();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = reviewRepository.findAllByUserId(userId, pageable);

        return PageResponse.<ReviewResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream().map(reviewMapper::toReviewResponse).toList())
                .build();
    }

    void updateRating(String listingId, Review review) {
        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() -> new AppException(ErrorCode.LISTING_NOT_FOUND));
        log.info("Task is being performed");
        listing.setRatingCount(listing.getRatingCount() + 1);
        log.info("ratingCount: " + listing.getRatingCount().toString() );
        listing.setRatingPoint((listing.getRatingPoint() + review.getRatingPoint()) / listing.getRatingCount());
        log.info("ratingPoint: " + listing.getRatingPoint().toString() );
        listing.getReviewIds().add(review);
        log.info("reviewIds: " + listing.getReviewIds().toString());
        
        listingRepository.save(listing);

    }

}
