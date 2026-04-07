package com.banking.smartbank.service.impl;

import com.banking.smartbank.domain.User;
import com.banking.smartbank.dto.response.UserResponse;
import com.banking.smartbank.exception.ResourceNotFoundException;
import com.banking.smartbank.mapper.UserMapper;
import com.banking.smartbank.repository.UserRepository;
import com.banking.smartbank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toResponse).toList();
    }
}
