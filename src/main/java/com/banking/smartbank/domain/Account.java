package com.banking.smartbank.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "Accounts")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String accountNumber;

    private String type;

    @Column(precision = 19, scale = 2)
    private BigDecimal balance;

    private String currency;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

}
