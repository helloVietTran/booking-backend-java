package com.booking.listingservice.mapper;

import org.mapstruct.Mapper;

import com.booking.listingservice.dto.response.RecentSearchResponse;
import com.booking.listingservice.entity.RecentSearch;

@Mapper(componentModel = "spring")
public interface RecentSearchMapper {
    RecentSearchResponse toRecentSearchResponse(RecentSearch recentSearch);
}
