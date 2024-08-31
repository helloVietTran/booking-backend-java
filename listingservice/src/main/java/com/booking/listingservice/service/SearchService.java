package com.booking.listingservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.booking.listingservice.dto.request.RecentSearchCreationRequest;
import com.booking.listingservice.dto.response.ListingResponse;
import com.booking.listingservice.dto.response.RecentSearchResponse;
import com.booking.listingservice.entity.Listing;
import com.booking.listingservice.entity.RecentSearch;
import com.booking.listingservice.entity.RecentSearchResult;
import com.booking.listingservice.entity.Region;
import com.booking.listingservice.exception.AppException;
import com.booking.listingservice.exception.ErrorCode;
import com.booking.listingservice.mapper.ListingMapper;
import com.booking.listingservice.mapper.RecentSearchMapper;
import com.booking.listingservice.repository.ListingRepositoryCustomImpl;
import com.booking.listingservice.repository.RecentSearchRepository;
import com.booking.listingservice.repository.RegionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {

    ListingRepositoryCustomImpl listingRepositoryCustomImpl;
    RecentSearchRepository recentSearchRepository;
    RegionRepository regionRepository;

    RecentSearchMapper recentSearchMapper;
    ListingMapper listingMapper;

    TokenService tokenService;

    public List<ListingResponse> search(String city, LocalDateTime checkin, LocalDateTime checkout, Integer adultCount,
            Integer childrenCount, Integer roomCount, Double minPrice, Double maxPrice,
            List<String> amenities, Integer minRating, Integer bedroomCount,
            Integer bathroomCount, List<String> listingTypes, Boolean hasBreakfast) {
                
        List<Listing> listings = listingRepositoryCustomImpl.searchListings(city, checkin, checkout, adultCount,
                childrenCount, roomCount, minPrice, maxPrice, amenities, minRating, bedroomCount, bathroomCount,
                listingTypes, hasBreakfast);

        return listings.stream().map(listingMapper::toListingResponse).toList();
    }

    public RecentSearchResponse getRecentSearch() {
        String userId = tokenService.getUserIdFromToken();
        Optional<RecentSearch> reccentSearchOptional = recentSearchRepository.findFirstByUserId(userId);

        if (!reccentSearchOptional.isPresent()) {
            throw new AppException(ErrorCode.RECENT_SEARCH_NOT_FOUND);
        }
        RecentSearch recentSearch = reccentSearchOptional.get();

        return recentSearchMapper.toRecentSearchResponse(recentSearch);
    }

    public RecentSearchResponse createRecentSearch(RecentSearchCreationRequest request) {
        Optional<Region> regionOptional = regionRepository.findByName(request.getCity());
        if (!regionOptional.isPresent()) {
            throw new AppException(ErrorCode.LISTINGTYPE_NOT_FOUND);
        }

        Region region = regionOptional.get();

        RecentSearchResult recentSearchResult = RecentSearchResult.builder()
                .city(request.getCity())
                .checkinDate(request.getCheckin())
                .checkoutDate(request.getCheckout())
                .guestsCount(request.getChildrensCount() + request.getAdultsCount())
                .imgSrc(region.getImgSrc())
                .build();

        RecentSearch recentSearch = RecentSearch.builder()
                .userId(tokenService.getUserIdFromToken())
                .build();
        recentSearch.getRecentSearchList().add(recentSearchResult);
        recentSearchRepository.save(recentSearch);

        return recentSearchMapper.toRecentSearchResponse(recentSearch);
    }

}
