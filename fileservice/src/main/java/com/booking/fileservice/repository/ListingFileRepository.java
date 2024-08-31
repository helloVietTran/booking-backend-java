package com.booking.fileservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.fileservice.entity.ListingFile;

public interface ListingFileRepository extends JpaRepository<ListingFile, Long> {
    
}
