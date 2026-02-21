package com.sentinelwatch.scan_service.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.sentinelwatch.scan_service.client.VirusTotalClient;
import com.sentinelwatch.scan_service.dto.VirusTotalResponse;
import com.sentinelwatch.scan_service.model.ScanResult;
import com.sentinelwatch.scan_service.repository.ScanRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@Service
public class ScanService {

    private final VirusTotalClient virusTotalClient;
    private final ScanRepository scanRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${virustotal.api.key:clave_de_emergencia}")
    private String apiKey;

    public ScanService(VirusTotalClient virusTotalClient, 
                       ScanRepository scanRepository, 
                       SimpMessagingTemplate messagingTemplate) {
        this.virusTotalClient = virusTotalClient;
        this.scanRepository = scanRepository;
        this.messagingTemplate = messagingTemplate;
    }

   @CircuitBreaker(name = "virusTotalService", fallbackMethod = "fallbackVirusTotal")
    public VirusTotalResponse checkFileStatus(String hash) {
               
        try {
            VirusTotalResponse response = virusTotalClient.getFileReport(apiKey, hash);

            ScanResult result = scanRepository.findByFileHash(hash).orElse(new ScanResult());
            result.setFileHash(hash);
            result.setScanDate(LocalDateTime.now());
            
            if (response != null && response.getData().getAttributes().getLast_analysis_stats().getMalicious() > 0) {
                result.setStatus("MALICIOUS");
            } else {
                result.setStatus("CLEAN");
            }
            
            scanRepository.save(result);

            messagingTemplate.convertAndSend("/topic/notifications", 
                "SUCCESS_SCAN: Análisis completado para el hash: " + hash);

            return response;
        } catch (feign.FeignException.NotFound e) {
            ScanResult unknown = scanRepository.findByFileHash(hash).orElse(new ScanResult());
            unknown.setFileHash(hash);
            unknown.setScanDate(LocalDateTime.now());
            unknown.setStatus("CLEAN");
            scanRepository.save(unknown);

            messagingTemplate.convertAndSend("/topic/notifications", 
                "INFO: Archivo nuevo detectado. No hay registros previos.");
            return null;
        }
    }

    public VirusTotalResponse fallbackVirusTotal(String hash, Throwable t) {
        System.out.println("ALERTA: Circuit Breaker activado. Motivo: " + t.getMessage());
        messagingTemplate.convertAndSend("/topic/notifications", 
            "CRITICAL: Servicio de inteligencia fuera de línea. Usando respaldo local.");
        return null; 
    }

    public List<ScanResult> getHistory() {
        return scanRepository.findTop10ByOrderByScanDateDesc();
    }

    public void clearHistory() {
        scanRepository.deleteAll(); 
    }

    
}
