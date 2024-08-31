package com.booking.listingservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.ListingType;

@Repository
public interface ListingTypeRepository extends MongoRepository<ListingType, String>  {
    
}
