package com.allmyles.secretserver.repository;

import com.allmyles.secretserver.model.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecretRepository extends JpaRepository<Secret, Long> {
    Optional<Secret> findByText(String text);

    Secret save(Secret secret);

}
