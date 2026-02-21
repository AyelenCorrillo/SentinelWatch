package com.sentinelwatch.scan_service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.sentinelwatch.scan_service.util.HashUtils;

@ExtendWith(MockitoExtension.class)
public class HashUtilsTest {

    @Mock
    private MultipartFile mockFile; 

    @Test
    public void testCalculateSHA256_Success() throws Exception {
        
        byte[] content = "test".getBytes();
        when(mockFile.getBytes()).thenReturn(content);

        String expectedHash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        String resultHash = HashUtils.calculateSHA256(mockFile);

        assertEquals(expectedHash, resultHash, "El hash generado no coincide con el SHA-256 real de 'test'");
    }

    @Test
    public void testCalculateSHA256_EmptyFile() throws Exception {

        byte[] emptyContent = new byte[0];
        when(mockFile.getBytes()).thenReturn(emptyContent);

        String resultHash = HashUtils.calculateSHA256(mockFile);
        String expectedEmptyHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        
        assertEquals(expectedEmptyHash, resultHash, "El hash para un archivo vacío no es el esperado");
    }

    @Test
    public void testCalculateSHA256_IOException() throws Exception {
        
        when(mockFile.getBytes()).thenThrow(new IOException("Archivo corrupto o bloqueado"));
        assertThrows(RuntimeException.class, () -> {
            HashUtils.calculateSHA256(mockFile);
        }, "Se esperaba una RuntimeException cuando la lectura del archivo falla");
    }
    
}
