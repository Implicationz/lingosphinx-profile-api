package com.lingosphinx.profile.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "profile",
        uniqueConstraints = @UniqueConstraint(columnNames = "user_id"),
        indexes = {
                @Index(name = "idx_profile_name", columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    @Builder.Default
    private String name = "New User";

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "subscription_id", referencedColumnName = "id")
    private Subscription subscription = null;

    @Builder.Default
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileLanguage> languages = new ArrayList<>();

    public static Profile fromUserId(UUID userId) {
        return Profile.builder().userId(userId).build();
    }
}