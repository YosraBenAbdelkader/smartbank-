package com.banking.smartbank.controller;

import com.banking.smartbank.dto.response.AiAnalysisResponse;
import com.banking.smartbank.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("/analyze/{accountId}")
    public ResponseEntity<AiAnalysisResponse> analyzeAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(aiService.analyzeAccount(accountId));
    }

    @GetMapping("/anomalies/{accountId}")
    public ResponseEntity<AiAnalysisResponse> detectAnomalies(@PathVariable Long accountId) {
        return ResponseEntity.ok(aiService.detectAnomalies(accountId));
    }
}
