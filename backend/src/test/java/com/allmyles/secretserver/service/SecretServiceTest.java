package com.allmyles.secretserver.service;

import com.allmyles.secretserver.repository.SecretRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecretServiceTest {

        @Mock
        private SecretRepository secretRepository;

        @InjectMocks
        private SecretService secretService;

    @Test
    void testGetSecretsByHash_ReturnEmptyList() {
        // Given
        String testHash = "testHash";
        int expected = 0;

        when(secretRepository.findByHash(testHash)).thenReturn(Optional.empty());

        // When
        int actual = secretService.getSecretByHash(testHash)
                .map(Collections::singletonList)
                .orElse(Collections.emptyList())
                .size();

        // Then
        assertEquals(expected, actual);
    }

}