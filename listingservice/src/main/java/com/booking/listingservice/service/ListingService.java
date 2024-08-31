package com.booking.listingservice.service;

import java.util.Optional;
import java.util.List;

import org.bson.Document;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.booking.listingservice.dto.request.ListingCreationRequest;
import com.booking.listingservice.dto.request.UpdateListingImageRequest;
import com.booking.listingservice.dto.response.ListingResponse;
import com.booking.listingservice.dto.response.PageResponse;
import com.booking.listingservice.entity.Listing;
import com.booking.listingservice.entity.ListingType;
import com.booking.listingservice.exception.AppException;
import com.booking.listingservice.exception.ErrorCode;
import com.booking.listingservice.mapper.ListingMapper;
import com.booking.listingservice.repository.ListingRepository;
import com.booking.listingservice.repository.ListingTypeRepository;
import com.booking.listingservice.repository.ReviewRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingService {
    
    ListingRepository listingRepository;
    ListingTypeRepository listingTypeRepository;
    ReviewRepository reviewRepository;

    ListingMapper listingMapper;
    TokenService tokenService;

    MongoTemplate mongoTemplate;

    public ListingResponse createListing(ListingCreationRequest request){
        
        ListingType listingType = listingTypeRepository.findById(request.getListingTypeId())
                                .orElseThrow(()-> new AppException(ErrorCode.LISTINGTYPE_NOT_FOUND));
        
        String ownerId= tokenService.getUserIdFromToken();

        Listing listing = listingMapper.toListing(request);
        listing.setOwnerId(ownerId);
        listing.setListingTypeLabel(listingType.getLabel());

        listing.setListingType(listingType.getName());

        return listingMapper.toListingResponse(listingRepository.save(listing));
    }   

    public void deleteMyListing(String listingId){
        String ownerId = tokenService.getUserIdFromToken();

        listingRepository.deleteByIdAndOwnerId(listingId, ownerId);
    }

    public ListingResponse getListing(String id){
        Optional<Listing> optionalListing = listingRepository.findById(id);
        if(!optionalListing.isPresent()){
            throw new AppException(ErrorCode.LISTING_NOT_FOUND);
        }
        ListingResponse listingResponse = listingMapper.toListingResponse(optionalListing.get());
        listingResponse.setReviews(optionalListing.get().getReviewIds());
        return listingResponse;
    }

    public void updateListingImages(UpdateListingImageRequest request){
        Listing listing = listingRepository.findById(request.getListingId())
                                .orElseThrow(()-> new AppException(ErrorCode.LISTING_NOT_FOUND));

        listing.setImgSrcs(request.getImgSrcs());

        listingRepository.save(listing);
    }

    public PageResponse<ListingResponse> getListings(int page, int size){
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = listingRepository.findAll(pageable);

        return PageResponse.<ListingResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream().map(listingMapper::toListingResponse).toList())
                .build();
    } 


    public PageResponse<ListingResponse>  getMyListings(int page, int size){
        String ownerId = tokenService.getUserIdFromToken();

        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        var pageData = listingRepository.findAllByOwnerId(ownerId, pageable);

        return PageResponse.<ListingResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .data(pageData.getContent().stream().map(listingMapper::toListingResponse).toList())
                .build();
    }

    public List<Document> findTop10HighestRated(){
          Aggregation agg = Aggregation.newAggregation(
                        Aggregation.match(Criteria.where("ratingCount").gt(10)),  
                        Aggregation.sort(Sort.Direction.DESC, "ratingPoint"),
                        Aggregation.limit(10)         
                        );
         AggregationResults<Document> results = mongoTemplate.aggregate(agg, "listing", Document.class);

         return results.getMappedResults();  
    }
}
