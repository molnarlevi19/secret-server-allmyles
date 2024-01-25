package com.allmyles.secretserver.repository;

import com.allmyles.secretserver.model.Secret;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@DataJpaTest
class SecretRepositoryTest {

    @Mock
    private SecretRepository secretRepository;

    @Test
    void testFindBySecretText_ReturnSecret() {
        // Given
        String searchText = "testText";
        Secret secret = new Secret();
        secret.setSecretText(searchText);

        when(secretRepository.findBySecretText(searchText)).thenReturn(Optional.of(secret));

        // When
        Optional<Secret> foundSecret = secretRepository.findBySecretText(searchText);

        // Then
        assertThat(foundSecret).isPresent();
        assertThat(foundSecret.get().getSecretText()).isEqualTo(searchText);
    }

    @Test
    void testFindByHash_ReturnSecret() {
        // Given
        String hash = "testHash";
        Secret secret = new Secret();
        secret.setHash(hash);

        when(secretRepository.findByHash(hash)).thenReturn(Optional.of(secret));

        // When
        Optional<Secret> foundSecret = secretRepository.findByHash(hash);

        // Then
        assertThat(foundSecret).isPresent();
        assertThat(foundSecret.get().getHash()).isEqualTo(hash);
    }
}