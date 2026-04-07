package com.banking.smartbank.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAnalysisResponse {
    private Long accountId;
    private String analysis;
    private String anomalies;
    private LocalDateTime analyzedAt;
}