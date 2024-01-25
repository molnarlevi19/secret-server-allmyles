package com.allmyles.secretserver.controller;

import com.allmyles.secretserver.model.Secret;
import com.allmyles.secretserver.service.SecretService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecretControllerTest {

    @Mock
    private SecretService secretService;


    @InjectMocks
    private SecretController secretController;

    @Test
    void testSendSecret_ValidSecret_ReturnsCreatedResponse() {
        // Given
        Secret testSecret = new Secret();
        when(secretService.saveSecret(any(Secret.class))).thenReturn("testSecretId");

        // When
        ResponseEntity<String> response = secretController.sendSecret(testSecret);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("testSecretId", response.getBody());
    }

    @Test
    void testSendSecret_ExceptionThrown_ReturnsInternalServerError() {
        // Given
        Secret testSecret = new Secret();
        when(secretService.saveSecret(any(Secret.class))).thenThrow(new RuntimeException("Test exception"));

        // When
        ResponseEntity<String> response = secretController.sendSecret(testSecret);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).contains("An error occurred while saving the secret."));
    }

}
