package com.sentinelwatch.scan_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sentinelwatch.scan_service.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
}
