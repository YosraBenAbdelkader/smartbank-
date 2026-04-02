package com.banking.smartbank.repository;

import com.banking.smartbank.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
