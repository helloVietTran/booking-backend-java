package com.booking.listingservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.booking.listingservice.dto.request.ListingCreationRequest;
import com.booking.listingservice.dto.response.ListingResponse;
import com.booking.listingservice.entity.Listing;

@Mapper(componentModel = "spring")
public interface ListingMapper {
    Listing toListing(ListingCreationRequest request);

    @Mapping(target = "reviews", ignore = true)
    ListingResponse toListingResponse(Listing listing);
}


