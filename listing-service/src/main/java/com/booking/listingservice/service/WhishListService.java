package com.booking.listingservice.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.booking.listingservice.dto.request.AddToWhishListRequest;
import com.booking.listingservice.dto.request.RemoveFromWhishListRequest;
import com.booking.listingservice.dto.response.WhishListResponse;
import com.booking.listingservice.entity.Listing;
import com.booking.listingservice.entity.WhishList;
import com.booking.listingservice.exception.AppException;
import com.booking.listingservice.exception.ErrorCode;
import com.booking.listingservice.mapper.WhishListMapper;
import com.booking.listingservice.repository.ListingRepository;
import com.booking.listingservice.repository.WhishListRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WhishListService {
    ListingRepository listingRepository;
    WhishListRepository whishListRepository;
    WhishListMapper whishListMapper;
    
    TokenService tokenService;
    
    public WhishListResponse addToWhishList(AddToWhishListRequest request){
        Listing listing = listingRepository.findById(request.getListingId())
                            .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        Optional<WhishList> optionalWhishList = whishListRepository.findFirstByUserId(tokenService.getUserIdFromToken());

        WhishList whishList;

        if (optionalWhishList.isPresent()) {
            whishList = optionalWhishList.get();
        } else {
            whishList = WhishList.builder()
                                .userId(tokenService.getUserIdFromToken())
                                .favouriteList(new ArrayList<>())
                                .updatedAt(Instant.now())
                                .build();
        }

        if (!whishList.getFavouriteList().contains(listing)) {
            whishList.getFavouriteList().add(listing);
        }

        whishListRepository.save(whishList);

        return whishListMapper.toWhishListResponse(whishList);
    }
    
    public WhishListResponse getWhishList(){
        Optional<WhishList> whishListOptional = whishListRepository.findFirstByUserId(tokenService.getUserIdFromToken());
        if(!whishListOptional.isPresent()){
           throw new AppException(ErrorCode.YOUR_WHISH_LIST_NOT_FOUND);
        }

        WhishList whishList = whishListOptional.get();
        return whishListMapper.toWhishListResponse(whishList);
    }

    public WhishListResponse removeFromWhishList(RemoveFromWhishListRequest request){
        Listing listing = listingRepository.findById(request.getListingId())
                    .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        Optional<WhishList> optionalWhishList = whishListRepository.findFirstByUserId(tokenService.getUserIdFromToken());

        if (!optionalWhishList.isPresent()) {
            throw new AppException(ErrorCode.YOUR_WHISH_LIST_NOT_FOUND);
        } 

        WhishList whishList = optionalWhishList.get();

        if (whishList.getFavouriteList().contains(listing)) {
            whishList.getFavouriteList().remove(listing);
        }

        whishListRepository.save(whishList);

        return whishListMapper.toWhishListResponse(whishList);
    }
}
