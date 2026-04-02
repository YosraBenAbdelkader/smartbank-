package com.banking.smartbank.service;

import com.banking.smartbank.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse findById(Long id);
    List<UserResponse> findAll();
}
