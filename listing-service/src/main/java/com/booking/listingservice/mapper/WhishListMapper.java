package com.booking.listingservice.mapper;

import org.mapstruct.Mapper;

import com.booking.listingservice.dto.response.WhishListResponse;
import com.booking.listingservice.entity.WhishList;

@Mapper(componentModel = "spring")
public interface WhishListMapper {
    WhishListResponse toWhishListResponse(WhishList whishList);
}
