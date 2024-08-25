package com.booking.listingservice.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.Listing;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String> {
    Page<Listing> findAll(Pageable pageable);

    Page<Listing> findAllByOwnerId(String userId, Pageable pageable);

    void deleteByIdAndOwnerId(String listingId,String ownerId);

    @Query("{ 'listingType.$id': ?0 }")
    List<Listing> findByListingType(String listingType);
}


