package com.allmyles.secretserver.controller;

import com.allmyles.secretserver.logger.Logger;
import com.allmyles.secretserver.model.Secret;
import com.allmyles.secretserver.repository.SecretRepository;
import com.allmyles.secretserver.service.SecretService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/all")
    public List<Secret> getAllSecrets(){
        return secretService.findAll();
    }
    @GetMapping("/secret")
    public ResponseEntity<Secret> getSecret(String text){
        Optional<Secret> optionalSecret = secretRepository.findByText(text);

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

}
