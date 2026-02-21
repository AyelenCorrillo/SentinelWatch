package com.sentinelwatch.scan_service;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.sentinelwatch.scan_service.controller.ScanViewController;
import com.sentinelwatch.scan_service.service.ScanService;

@WebMvcTest(ScanViewController.class)
@AutoConfigureMockMvc
public class ScanViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ScanService scanService; 

    @Test
    @WithMockUser
    public void testIndexPageLoadsCorrectly() throws Exception {
        
        when(scanService.getHistory()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/"))
                .andExpect(status().isOk()) 
                .andExpect(view().name("index")) 
                .andExpect(model().attributeExists("historial")); 
    }

    @Test
    public void testLoginPageExists() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login")); 
    }
    
    @Test
    @WithMockUser
    public void testScanHash_WhenServiceFails_ShouldShowErrorMessage() throws Exception {
       
        when(scanService.checkFileStatus("hash_de_prueba"))
            .thenThrow(new RuntimeException("Error de conexión con VirusTotal"));

        
        mockMvc.perform(post("/scan")
                .param("hash", "hash_de_prueba")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage")) 
                .andExpect(view().name("index"));
    }
    
}
