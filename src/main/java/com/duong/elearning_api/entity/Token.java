package com.duong.elearning_api.entity;

import com.duong.elearning_api.common.TokenType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Token extends BaseEntity{
    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false, length = 50)
    @Builder.Default
    private TokenType tokenType = TokenType.REFRESH_TOKEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean revoked = false;
}
