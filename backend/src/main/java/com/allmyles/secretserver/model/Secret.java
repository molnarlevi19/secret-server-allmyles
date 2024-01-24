package com.allmyles.secretserver.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "secret")
public class Secret {

    Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long secretId;

    @Column(nullable = false)
    private String hashedText;

    @Column(nullable = false)
    private String originalText;

    private int remainingViews;

    private LocalDateTime expiryTime;

    @Transient
    private Integer expiryTimeInMinutes;

}
