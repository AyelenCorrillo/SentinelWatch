package com.sentinelwatch.scan_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ScanRequest {

    @NotBlank(message = "El hash no puede estar vacío")
    @Size(min = 64, max = 64, message = "Un hash SHA-256 debe tener exactamente 64 caracteres")
    @Pattern(regexp = "^[a-fA-F0-9]+$", message = "El hash solo puede contener caracteres hexadecimales")
    private String hash;
    
}
