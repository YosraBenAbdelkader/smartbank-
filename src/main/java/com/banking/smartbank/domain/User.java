package com.banking.smartbank.domain;

import com.banking.smartbank.domain.enums.Role;
import com.banking.smartbank.domain.enums.TransferStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Data
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 100, nullable = false)
    private String firstName;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(length = 50, nullable = false)
    private Role role = Role.USER;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

}
