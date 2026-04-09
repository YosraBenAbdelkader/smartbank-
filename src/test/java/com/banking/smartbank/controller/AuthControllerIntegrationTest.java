package com.banking.smartbank.controller;

import com.banking.smartbank.dto.request.LoginRequest;
import com.banking.smartbank.dto.request.RegisterRequest;
import com.banking.smartbank.dto.response.AuthResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void register_ShouldReturn201_WhenValidRequest() {
        // GIVEN
        RegisterRequest request = new RegisterRequest("test@smartbank.com", "Test1234!", "Test", "User");

        // WHEN
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/register",
                request,
                AuthResponse.class
        );

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().token()).isNotBlank();
        assertThat(response.getBody().email()).isEqualTo("test@smartbank.com");
    }

    @Test
    void login_ShouldReturn200_WhenValidCredentials() {
        // GIVEN — d'abord register
        RegisterRequest registerRequest = new RegisterRequest("login@smartbank.com", "Test1234!", "Login", "User");
        restTemplate.postForEntity("/api/v1/auth/register", registerRequest, AuthResponse.class);

        // WHEN — puis login
        LoginRequest loginRequest = new LoginRequest("login@smartbank.com", "Test1234!");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                "/api/v1/auth/login",
                loginRequest,
                AuthResponse.class
        );

        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().token()).isNotBlank();
    }

    @Test
    void login_ShouldReturn403_WhenInvalidCredentials() {
        // GIVEN
        LoginRequest request = new LoginRequest("wrong@smartbank.com", "WrongPassword!");

        // WHEN
        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/v1/auth/login",
                request,
                String.class
        );

        // THEN
        assertThat(response.getStatusCode().is4xxClientError()).isTrue();
    }
}