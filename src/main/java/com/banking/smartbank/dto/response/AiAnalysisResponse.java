package com.banking.smartbank.dto.response;

import java.time.LocalDateTime;

public record AiAnalysisResponse(
        Long accountId,
        String analysis,
        String anomalies,
        LocalDateTime analyzedAt
) {}