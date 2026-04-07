package com.banking.smartbank.service;

import com.banking.smartbank.domain.User;
import com.banking.smartbank.domain.enums.Role;
import com.banking.smartbank.dto.response.UserResponse;
import com.banking.smartbank.exception.ResourceNotFoundException;
import com.banking.smartbank.mapper.UserMapper;
import com.banking.smartbank.repository.UserRepository;
import com.banking.smartbank.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("yosra@smartbank.com")
                .firstName("Yosra")
                .lastName("Benabdelkader")
                .role(Role.CLIENT)
                .build();

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setEmail("yosra@smartbank.com");
        userResponse.setFirstName("Yosra");
        userResponse.setLastName("Benabdelkader");
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // WHEN
        UserResponse result = userService.findById(1L);

        // THEN
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("yosra@smartbank.com");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenUserNotFound() {
        // GIVEN
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // GIVEN
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // WHEN
        List<UserResponse> results = userService.findAll();

        // THEN
        assertThat(results).asList().hasSize(1);
        assertThat(results.get(0).getEmail()).isEqualTo("yosra@smartbank.com");
    }
}