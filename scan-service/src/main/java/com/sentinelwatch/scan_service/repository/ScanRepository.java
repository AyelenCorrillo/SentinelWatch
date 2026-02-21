package com.sentinelwatch.scan_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sentinelwatch.scan_service.model.ScanResult;

@Repository
public interface ScanRepository extends JpaRepository<ScanResult, Long> {

    List<ScanResult> findTop10ByOrderByScanDateDesc();
    Optional<ScanResult> findByFileHash(String fileHash);

    
}
