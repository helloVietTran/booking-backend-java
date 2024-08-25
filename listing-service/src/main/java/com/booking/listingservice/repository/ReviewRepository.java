package com.booking.listingservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {
    Page<Review> findAll(Pageable pageable);

    Page<Review> findAllByUserId(String userId, Pageable pageable);

    void deleteByUserId(String userId);

    Page<Review> findAllByListingId(String lisingId, Pageable pageable);
} 
