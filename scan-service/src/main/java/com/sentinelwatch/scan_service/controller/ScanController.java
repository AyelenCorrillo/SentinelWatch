package com.sentinelwatch.scan_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sentinelwatch.scan_service.dto.VirusTotalResponse;
import com.sentinelwatch.scan_service.service.ScanService;

@RestController
@RequestMapping("/api/v1/scan")
public class ScanController {

    private final ScanService scanService;

    public ScanController(ScanService scanService) {
        this.scanService = scanService;
    }

    @GetMapping("/check/{hash}")
    public VirusTotalResponse checkHash(@PathVariable String hash) {
        
        return scanService.checkFileStatus(hash);
    }
    
}
