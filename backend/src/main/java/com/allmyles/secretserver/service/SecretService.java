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

@Service
public class SecretService {

    private final SecretRepository secretRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    public SecretService(SecretRepository secretRepository) {
        this.secretRepository = secretRepository;
    }


    public String saveSecret(Secret secret) {
        Secret newSecret = new Secret();

        String text = secret.getSecretText();
        int remainingViews = secret.getRemainingViews();
        Integer expiryTimeInMinutes = secret.getExpiryTimeInMinutes();


        String hashedText = hashTextWithBcrypt(text);

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

        Secret savedSecret = secretRepository.save(newSecret);
        return Base64.getUrlEncoder().encodeToString(savedSecret.getHash().getBytes());
    }

    private String hashTextWithBcrypt(String text) {
        return passwordEncoder.encode(text);
    }

    public Optional<Secret> getSecretByHash(String hash) {
        return secretRepository.findByHash(hash);
    }

    public void decreaseRemainingViews(String hash) {
        Secret secret = secretRepository.findByHash(hash)
                .orElseThrow(() -> new EntityNotFoundException("Secret not found with hash: " + hash));

        int remainingViews = secret.getRemainingViews();

        if (remainingViews > 0) {
            secret.setRemainingViews(remainingViews - 1);
            secretRepository.save(secret);
        }
    }


    /*public List<Secret> findAll() {
        return secretRepository.findAll();
    }*/
}
