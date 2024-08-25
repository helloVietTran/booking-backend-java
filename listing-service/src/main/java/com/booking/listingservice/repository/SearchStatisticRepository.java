package com.booking.listingservice.repository;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.SearchStatistic;

@Repository
public interface SearchStatisticRepository extends MongoRepository<SearchStatistic, String> {
    
}
