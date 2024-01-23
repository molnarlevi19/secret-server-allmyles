package com.allmyles.secretserver.service;

import com.allmyles.secretserver.model.Secret;
import com.allmyles.secretserver.repository.SecretRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

        String text = secret.getText();
        int remainingViews = secret.getRemainingViews();
        Integer expiryTimeInMinutes = secret.getExpiryTimeInMinutes();

        String hashedText = hashTextWithBcrypt(text);

        newSecret.setText(hashedText);
        newSecret.setRemainingViews(remainingViews);

        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(expiryTimeInMinutes);
        newSecret.setExpiryTime(expiryTime);

        Secret savedSecret = secretRepository.save(newSecret);
        return savedSecret.getSecretId().toString();
    }

    private String hashTextWithBcrypt(String text) {
        return passwordEncoder.encode(text);
    }

    public List<Secret> findAll() {
        return secretRepository.findAll();
    }
}
