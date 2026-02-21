package com.sentinelwatch.scan_service.dto;

import java.util.Map;

import lombok.Data;

@Data
public class VirusTotalResponse {

    private DataDTO data;

    @Data
    public static class DataDTO {
        private String id;
        private AttributesDTO attributes;
    }

    @Data
    public static class AttributesDTO {
        private Map<String, AnalysisResultDTO> last_analysis_results;
        private StatsDTO last_analysis_stats;
    }

    @Data
    public static class StatsDTO {
        private int harmless;
        private int malicious;
        private int suspicious;
        private int undetected;
    }

    @Data
    public static class AnalysisResultDTO {
        private String engine_name;
        private String category;
        private String result;
    }
    
}
