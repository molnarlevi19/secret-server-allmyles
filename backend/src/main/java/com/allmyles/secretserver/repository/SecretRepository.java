package com.allmyles.secretserver.repository;

import com.allmyles.secretserver.model.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The repository interface for managing {@link Secret} entities.
 * Extends {@link JpaRepository} for basic CRUD operations on secrets.
 */
public interface SecretRepository extends JpaRepository<Secret, Long> {

    Optional<Secret> findBySecretText(String secretText);

    Optional<Secret> findByHash(String hash);
}
