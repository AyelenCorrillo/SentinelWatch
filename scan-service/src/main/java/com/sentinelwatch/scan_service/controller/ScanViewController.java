package com.sentinelwatch.scan_service.controller;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.sentinelwatch.scan_service.service.ScanService;

@Controller
public class ScanViewController {

    private final ScanService scanService;

    public ScanViewController(ScanService scanService) {
        this.scanService = scanService;
    }

    @PostMapping("/scan")
    public String scanHash(@RequestParam(value = "hash", required = false) String hash,
                        @RequestParam(value = "archivo", required = false) MultipartFile archivo,
                        Model model) {
        try {
            String hashFinal = hash;

            if (archivo != null && !archivo.isEmpty()) {
                hashFinal = com.sentinelwatch.scan_service.util.HashUtils.calculateSHA256(archivo);
            }

            if (hashFinal == null || hashFinal.isEmpty()) {
                model.addAttribute("errorMessage", "Debes proporcionar un hash o un archivo.");
            } else {
                var response = scanService.checkFileStatus(hashFinal);

                if (response == null) {
                    model.addAttribute("errorMessage", "AVISO: Este archivo no tiene registros previos en la base de datos global.");
                } else {
                    model.addAttribute("resultado", response);
                }
                model.addAttribute("hash", hashFinal);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error al procesar el archivo o hash." + e.getMessage());
        }

        model.addAttribute("historial", scanService.getHistory());
        return "index";
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("historial", scanService.getHistory());
            model.addAttribute("errorMessage", null);
        }catch (Exception e) {
            model.addAttribute("historial", new ArrayList<>());
        }
        return "index"; 
    }

    @GetMapping("/login")
    public String login() {
        return "login"; 
    }

    @PostMapping("/clear-history")
    public String clearHistory() {
        scanService.clearHistory();
        return "redirect:/";
    }

    
}
