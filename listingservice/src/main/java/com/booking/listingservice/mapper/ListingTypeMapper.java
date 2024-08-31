package com.booking.listingservice.mapper;

import org.mapstruct.Mapper;

import com.booking.listingservice.dto.request.ListingTypeCreationRequest;
import com.booking.listingservice.dto.response.ListingTypeResponse;
import com.booking.listingservice.entity.ListingType;

@Mapper(componentModel = "spring")
public interface ListingTypeMapper {
    ListingType toListingType(ListingTypeCreationRequest request);

    ListingTypeResponse toListingTypeResponse(ListingType listingType);
}
