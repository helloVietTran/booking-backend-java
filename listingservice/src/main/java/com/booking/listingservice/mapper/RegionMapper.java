package com.booking.listingservice.mapper;

import org.mapstruct.Mapper;

import com.booking.listingservice.dto.request.RegionCreationRequest;
import com.booking.listingservice.dto.response.RegionResponse;
import com.booking.listingservice.entity.Region;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    Region toRegion(RegionCreationRequest request);

    RegionResponse toRegionResponse(Region region);

}
