package com.banking.smartbank.service;

import com.banking.smartbank.dto.response.AiAnalysisResponse;

public interface AiService {
    AiAnalysisResponse analyzeAccount(Long accountId);
    AiAnalysisResponse detectAnomalies(Long accountId);
}