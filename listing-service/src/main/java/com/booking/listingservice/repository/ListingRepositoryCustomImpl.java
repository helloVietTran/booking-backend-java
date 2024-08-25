package com.booking.listingservice.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.booking.listingservice.entity.Listing;

@Repository
public class ListingRepositoryCustomImpl implements ListingRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;// là lớp làm việc với mongodb

    private void addCityCriteria(Criteria criteria, String city) {
        if (city != null) {
            criteria.and("city").is(city);
        }
    }

    /** */ private void addDateCriteria(Criteria criteria, LocalDateTime checkin, LocalDateTime checkout) {
        if (checkin != null) {
            criteria.and("checkinTime").gte(checkin);
        }
        if (checkout != null) {
            criteria.and("checkoutTime").lte(checkout);// cần sửa
        }
    }

   /** */ private void addCountCriteria(Criteria criteria, Integer adultCount, Integer childrenCount, Integer roomCount) {
        if (adultCount != null) {
            criteria.and("guestsCount").gte(adultCount);
        }
        if (childrenCount != null) {
            criteria.and("childrenCount").gte(childrenCount);
        }
        if (roomCount != null) {
            criteria.and("bedroomsCount").gte(roomCount);
        }
    }

    private void addPriceCriteria(Criteria criteria, Double minPrice, Double maxPrice) {
        if (minPrice != null) {
            criteria.and("pricePerNight").gte(minPrice);
        }
        if (maxPrice != null) {
            criteria.and("pricePerNight").lte(maxPrice);
        }
    }

    private void addAmenitiesCriteria(Criteria criteria, List<String> amenities) {
        if (amenities != null && !amenities.isEmpty()) {
            criteria.and("amenities").in(amenities);
        }
    }

    private void addRatingCriteria(Criteria criteria, Integer minRating) {
        if (minRating != null) {
            criteria.and("rating").gte(minRating);
        }
    }

    private void addBedroomAndBathroomCriteria(Criteria criteria, Integer bedroomCount, Integer bathroomCount) {
        if (bedroomCount != null) {
            criteria.and("bedroomCount").gte(bedroomCount);
        }
        if (bathroomCount != null) {
            criteria.and("bathroomCount").gte(bathroomCount);
        }
    }

   /** */ private void addListingTypesCriteria(Criteria criteria, List<String> listingTypes) {
        if (listingTypes != null && !listingTypes.isEmpty()) {
            criteria.and("listingType.id").in(listingTypes);
        }
    }

    private void addBreakfastCriteria(Criteria criteria, Boolean hasBreakfast) {
        if (hasBreakfast != null) {
            criteria.and("hasBreakfast").is(hasBreakfast);
        }
    }

    @Override
    public List<Listing> searchListings(String city, LocalDateTime checkin, LocalDateTime checkout, Integer adultCount,
            Integer childrenCount, Integer roomCount, Double minPrice, Double maxPrice,
            List<String> amenities, Integer minRating, Integer bedroomCount,
            Integer bathroomCount, List<String> listingTypes, Boolean hasBreakfast) {

        Query query = new Query();
        Criteria criteria = new Criteria();

        addCityCriteria(criteria, city);
        addDateCriteria(criteria, checkin, checkout);
        addCountCriteria(criteria, adultCount, childrenCount, roomCount);
        addPriceCriteria(criteria, minPrice, maxPrice);
        addAmenitiesCriteria(criteria, amenities);
        addRatingCriteria(criteria, minRating);
        addBedroomAndBathroomCriteria(criteria, bedroomCount, bathroomCount);
        addListingTypesCriteria(criteria, listingTypes);
        addBreakfastCriteria(criteria, hasBreakfast);

        query.addCriteria(criteria);
        return mongoTemplate.find(query, Listing.class);
    }
}