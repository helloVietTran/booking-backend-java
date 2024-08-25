package com.booking.profileservice.repository;

import java.util.Optional;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import com.booking.profileservice.entity.UserProfile;


@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfile, String> {
    void deleteByUserId(String userId);

    Optional<UserProfile> findByUserId(String userId);
}
