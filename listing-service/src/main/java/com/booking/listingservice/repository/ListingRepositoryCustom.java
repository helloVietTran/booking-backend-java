package com.booking.listingservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.Listing;

@Repository
public interface ListingRepositoryCustom {
    List<Listing> searchListings(String city, LocalDateTime checkin, LocalDateTime checkout, Integer adultCount,
                                 Integer childrenCount, Integer roomCount, Double minPrice, Double maxPrice,
                                 List<String> amenities, Integer minRating, Integer bedroomCount, 
                                 Integer bathroomCount, List<String> listingTypes, Boolean hasBreakfast);
}