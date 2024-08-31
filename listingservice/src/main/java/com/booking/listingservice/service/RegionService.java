package com.booking.listingservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.booking.listingservice.dto.request.RegionCreationRequest;
import com.booking.listingservice.dto.response.RegionResponse;
import com.booking.listingservice.entity.Region;
import com.booking.listingservice.mapper.RegionMapper;
import com.booking.listingservice.repository.RegionRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegionService {
    RegionRepository regionRepository;

    RegionMapper regionMapper;

    public RegionResponse createRegion(RegionCreationRequest request) {
        Region region = regionMapper.toRegion(request);
        regionRepository.save(region);
        return regionMapper.toRegionResponse(region);
    }

    public List<RegionResponse> getRegions(String label){
        return regionRepository.findAllByLabel(label)
                        .stream()
                        .map(regionMapper::toRegionResponse)
                        .toList();
    }
    public List<RegionResponse> getAllRegion(){
        List<Region> regions = regionRepository.findAll();

        return regions.stream().map(regionMapper::toRegionResponse).toList();
    }
}
