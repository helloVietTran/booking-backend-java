package com.booking.listingservice.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.booking.listingservice.dto.request.ListingTypeCreationRequest;
import com.booking.listingservice.dto.response.ListingTypeResponse;
import com.booking.listingservice.entity.ListingType;
import com.booking.listingservice.mapper.ListingTypeMapper;
import com.booking.listingservice.repository.ListingTypeRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ListingTypeService {

    ListingTypeRepository listingTypeRepository;
    ListingTypeMapper listingTypeMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public ListingTypeResponse create(ListingTypeCreationRequest request){
        ListingType listingType = listingTypeMapper.toListingType(request);

        return listingTypeMapper.toListingTypeResponse(listingTypeRepository.save(listingType));
    }  


    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String id){
        listingTypeRepository.deleteById(id);
    }  

    public List<ListingTypeResponse> getAll(){
       return listingTypeRepository.findAll().stream().map(listingTypeMapper::toListingTypeResponse).toList();
    }
}
