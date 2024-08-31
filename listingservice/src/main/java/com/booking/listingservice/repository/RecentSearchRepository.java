package com.booking.listingservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.RecentSearch;

@Repository
public interface RecentSearchRepository extends MongoRepository<RecentSearch, String>{
    Optional<RecentSearch> findFirstByUserId(String userId);
}
