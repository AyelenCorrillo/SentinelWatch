package com.sentinelwatch.scan_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sentinelwatch.scan_service.dto.VirusTotalResponse;

@FeignClient(name = "virus-total-client", url = "https://www.virustotal.com/api/v3")
public interface VirusTotalClient {

    @GetMapping("/files/{id}")
    VirusTotalResponse getFileReport(
        @RequestHeader("x-apikey") String apiKey, 
        @PathVariable("id") String id
    );
    
}
