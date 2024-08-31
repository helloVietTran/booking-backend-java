package com.booking.fileservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booking.fileservice.entity.AvatarFile;

public interface AvatarFileRepository extends JpaRepository<AvatarFile, Long> {
    Optional<AvatarFile> findByUserId(String userId);
}
