package com.booking.listingservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.WhishList;

@Repository
public interface WhishListRepository extends MongoRepository<WhishList, String> {
    Optional<WhishList> findFirstByUserId(String userId);
}
