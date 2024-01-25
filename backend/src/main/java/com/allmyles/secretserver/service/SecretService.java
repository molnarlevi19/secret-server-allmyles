package com.allmyles.secretserver.service;

import com.allmyles.secretserver.model.Secret;
import com.allmyles.secretserver.repository.SecretRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

/**
 * Service class responsible for business logic related to {@link Secret} entities.
 */
@Service
public class SecretService {

    private final SecretRepository secretRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Constructs a new instance of SecretService.
     *
     * @param secretRepository The repository for managing secret entities.
     */
    @Autowired
    public SecretService(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }

    /**
     * Saves a new secret and returns its hashed text.
     *
     * @param secret The secret to be saved.
     * @return The hashed text of the saved secret.
     */
    public String saveSecret(Secret secret) {
        Secret newSecret = new Secret();

        String text = secret.getSecretText();
        int remainingViews = secret.getRemainingViews();
        Integer expiryTimeInMinutes = secret.getExpiryTimeInMinutes();


        String originalHash = hashTextWithBcrypt(text);
        String hashedText = Base64.getUrlEncoder().encodeToString(originalHash.getBytes());

        newSecret.setSecretText(text);
        newSecret.setHash(hashedText);
        newSecret.setRemainingViews(remainingViews);

        if (expiryTimeInMinutes != null && expiryTimeInMinutes > 0) {
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime expiresAt = createdAt.plusMinutes(expiryTimeInMinutes);

            newSecret.setCreatedAt(createdAt);
            newSecret.setExpiresAt(expiresAt);
        } else {
            newSecret.setExpiresAt(null);
        }

        secretRepository.save(newSecret);
        return hashedText;
    }

    /**
     * Hashes the provided text using BCrypt.
     *
     * @param text The text to be hashed.
     * @return The hashed text.
     */
    private String hashTextWithBcrypt(String text) {
        return passwordEncoder.encode(text);
    }

    /**
     * Retrieves a secret by its hash.
     *
     * @param hash The hash of the secret.
     * @return An optional containing the secret if found, empty otherwise.
     */
    public Optional<Secret> getSecretByHash(String hash) {
        return secretRepository.findByHash(hash);
    }

    /**
     * Decreases the remaining views of a secret identified by its hash.
     *
     * @param hash The hash of the secret.
     * @throws EntityNotFoundException if the secret is not found.
     */
    public void decreaseRemainingViews(String hash) {
        Secret secret = secretRepository.findByHash(hash)
                .orElseThrow(() -> new EntityNotFoundException("Secret not found with hash: " + hash));

        int remainingViews = secret.getRemainingViews();

        if (remainingViews > 0) {
            secret.setRemainingViews(remainingViews - 1);
            secretRepository.save(secret);
        }
    }

}
