package com.banking.smartbank.service.impl;

import com.banking.smartbank.domain.Account;
import com.banking.smartbank.domain.Transaction;
import com.banking.smartbank.dto.response.AiAnalysisResponse;
import com.banking.smartbank.exception.ResourceNotFoundException;
import com.banking.smartbank.repository.AccountRepository;
import com.banking.smartbank.repository.TransactionRepository;
import com.banking.smartbank.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    public AiServiceImpl(ChatClient.Builder chatClientBuilder,
                         AccountRepository accountRepository,
                         TransactionRepository transactionRepository) {
        this.chatClient = chatClientBuilder.build();
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public AiAnalysisResponse analyzeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        String prompt = """
                Tu es un expert financier. Analyse les transactions bancaires suivantes
                pour le compte %s (solde actuel: %s %s) :
                
                %s
                
                Fournis:
                1. Un résumé des habitudes de dépenses
                2. Les catégories principales de dépenses
                3. Des recommandations financières
                
                Réponds en français, de manière concise.
                """.formatted(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                formatTransactions(transactions)
        );

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AiAnalysisResponse.builder()
                .accountId(accountId)
                .analysis(response)
                .analyzedAt(LocalDateTime.now())
                .build();
    }

    @Override
    public AiAnalysisResponse detectAnomalies(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);

        String prompt = """
                Tu es un expert en détection de fraude bancaire.
                Analyse ces transactions pour le compte %s :
                
                %s
                
                Détecte les anomalies potentielles :
                - Transactions inhabituelles en montant
                - Fréquence anormale
                - Patterns suspects
                
                Réponds en français avec un niveau de risque (FAIBLE/MOYEN/ÉLEVÉ).
                """.formatted(
                account.getAccountNumber(),
                formatTransactions(transactions)
        );

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return AiAnalysisResponse.builder()
                .accountId(accountId)
                .anomalies(response)
                .analyzedAt(LocalDateTime.now())
                .build();
    }

    private String formatTransactions(List<Transaction> transactions) {
        if (transactions.isEmpty()) return "Aucune transaction trouvée.";

        return transactions.stream()
                .map(t -> "- %s | %s | %s | %s".formatted(
                        t.getCreatedAt(),
                        t.getType(),
                        t.getAmount(),
                        t.getDescription()))
                .collect(Collectors.joining("\n"));
    }
}