package com.booking.listingservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.booking.listingservice.entity.Region;
import com.booking.listingservice.exception.AppException;
import com.booking.listingservice.exception.ErrorCode;
import com.booking.listingservice.repository.RegionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticService {
    MongoTemplate mongoTemplate;

    RegionRepository regionRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public void statisticByRegion() {
       
        List<Document> results = mongoTemplate.getCollection("listing").aggregate(Arrays.asList(
                new Document("$group", new Document("_id", "$city")// nhóm trên trường city
                        .append("totalListings", new Document("$sum", 1)))))
                .into(new ArrayList<>());

        for (Document result : results) {
            String city = result.getString("_id");
            log.info("City: "+ city);
            int totalListings = result.getInteger("totalListings");
            Optional<Region> regionOptional = regionRepository.findByName(city);
            if(!regionOptional.isPresent()){
                throw new AppException(ErrorCode.REGION_NOT_FOUND);
            }
            Region region = regionOptional.get();

            region.setTotalListings(totalListings);
            
            regionRepository.save(region);
        }
    }

   
}
