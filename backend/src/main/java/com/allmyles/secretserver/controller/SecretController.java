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
public class SecretController {

    private final SecretService secretService;
    private final Logger logger;

    @Autowired
    public SecretController(SecretService secretService, Logger logger) {
        this.secretService = secretService;
        this.logger = logger;
    }

    /**
     * Endpoint for saving a secret.
     *
     * @param secret The secret to be saved.
     * @return ResponseEntity containing the created secret's ID or an error message.
     */
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

    /**
     * Endpoint for viewing a secretText based on its hash.
     *
     * @param hash The hash identifying the secret.
     * @return ResponseEntity containing the secret text or an error message.
     */
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