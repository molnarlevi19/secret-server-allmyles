package com.allmyles.secretserver.controller;

import com.allmyles.secretserver.logger.Logger;
import com.allmyles.secretserver.model.Secret;
import com.allmyles.secretserver.repository.SecretRepository;
import com.allmyles.secretserver.service.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/secrets")
@CrossOrigin(origins = "http://localhost:5173")
public class SecretController {

    private final SecretRepository secretRepository;
    private final SecretService secretService;
    private final Logger logger;

    @Autowired
    public SecretController(SecretRepository secretRepository, SecretService secretService, Logger logger) {
        this.secretRepository = secretRepository;
        this.secretService = secretService;
        this.logger = logger;
    }

    @GetMapping("/secret")
    public ResponseEntity<Secret> getSecret(String text){
        Optional<Secret> optionalSecret = secretRepository.findBySecretText(text);

        if (optionalSecret.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            Secret secret = optionalSecret.get();
            return new ResponseEntity<>(secret, HttpStatus.OK);
        }
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

    /*@PatchMapping("/secret/{hash}")
    public ResponseEntity<String> decreaseRemainingViews(@PathVariable String hash) {
        try {
            secretService.decreaseRemainingViews(hash);
            return new ResponseEntity<>("Remaining views decreased successfully", HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.logError("Error decreasing remaining views: {}" + e.getMessage());
            return new ResponseEntity<>("Secret not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.logError("Error decreasing remaining views: {}" + e.getMessage());
            return new ResponseEntity<>("An error occurred while decreasing remaining views", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @GetMapping("/secret/{hash}")
    public ResponseEntity<String> viewSecret(@PathVariable String hash) {
        String decodedHash = URLDecoder.decode(hash, StandardCharsets.UTF_8);
        Optional<Secret> optionalSecret = secretService.getSecretByHash(decodedHash);

        if (optionalSecret.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Secret secret = optionalSecret.get();

            // Check if remaining views are zero
            if (secret.getRemainingViews() == 0) {
                return new ResponseEntity<>("Secret is not viewable. Remaining views: 0", HttpStatus.FORBIDDEN);
            }

            // Check if expiration time is in the past
            if (secret.getExpiresAt() != null && secret.getExpiresAt().isBefore(LocalDateTime.now())) {
                return new ResponseEntity<>("Secret is not viewable. Expired", HttpStatus.FORBIDDEN);
            }

            // Decrease remaining views
            if (secret.getRemainingViews() > 0) {
                secretService.decreaseRemainingViews(decodedHash);
            }

            // Return the secret text if it's viewable
            return new ResponseEntity<>(secret.getSecretText(), HttpStatus.OK);
        }
    }


    /*@GetMapping("/all")
    public List<Secret> getAllSecrets(){
        return secretService.findAll();
    }*/

}
