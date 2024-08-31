package com.booking.listingservice.repository;

import java.util.Optional;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.Region;

@Repository
public interface RegionRepository extends MongoRepository<Region, String>{
    
    Optional<Region> findByName(String name);

    List<Region> findAllByLabel(String label);
}
