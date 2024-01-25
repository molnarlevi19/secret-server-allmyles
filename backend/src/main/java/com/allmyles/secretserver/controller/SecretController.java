package com.allmyles.secretserver.controller;

import com.allmyles.secretserver.logger.Logger;
import com.allmyles.secretserver.model.Secret;
import com.allmyles.secretserver.service.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/secrets")
@CrossOrigin(origins = "http://localhost:5173")
public class SecretController {

    private final SecretService secretService;
    private final Logger logger;

    @Autowired
    public SecretController(SecretService secretService, Logger logger) {
        this.secretService = secretService;
        this.logger = logger;
    }

    @PostMapping("/secret")
    public ResponseEntity<String> sendSecret(@RequestBody Secret secret){
        try {
            String secretId = secretService.saveSecret(secret);
            return new ResponseEntity<>(secretId, HttpStatus.CREATED);
        } catch (Exception e) {

            logger.logError("Error saving secret: {} " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving the secret.");
        }
    }

    @GetMapping("/secret/{hash}")
    public ResponseEntity<String> viewSecret(@PathVariable String hash) {
        try {

            Optional<Secret> optionalSecret = secretService.getSecretByHash(hash);

            if (optionalSecret.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                Secret secret = optionalSecret.get();

                if (secret.getRemainingViews() == 0) {
                    return new ResponseEntity<>("Secret is not viewable. Remaining views: 0", HttpStatus.FORBIDDEN);
                }

                if (secret.getExpiresAt() != null && secret.getExpiresAt().isBefore(LocalDateTime.now())) {
                    return new ResponseEntity<>("Secret is not viewable. Expired", HttpStatus.FORBIDDEN);
                }

                if (secret.getRemainingViews() > 0) {
                    secretService.decreaseRemainingViews(hash);
                }

                return new ResponseEntity<>(secret.getSecretText(), HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Error decoding hash", HttpStatus.BAD_REQUEST);
        }
    }

}