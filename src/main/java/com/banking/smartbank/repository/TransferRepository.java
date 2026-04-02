package com.banking.smartbank.repository;

import com.banking.smartbank.domain.Transfer;
import com.banking.smartbank.domain.enums.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySenderAccountId(Long senderAccountId);
    List<Transfer> findByStatus(TransferStatus status);
}
