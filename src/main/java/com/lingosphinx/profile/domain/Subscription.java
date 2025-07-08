package com.lingosphinx.profile.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Subscription.Type type = Subscription.Type.FREE;
    private String stripeId;

    public enum Type {
        FREE,
        PREMIUM
    }
}