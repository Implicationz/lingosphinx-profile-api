package com.lingosphinx.profile.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "profile_language",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"profile_id", "position"}),
                @UniqueConstraint(columnNames = {"profile_id", "language_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Builder.Default
    @Column(nullable = false)
    private int position = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
}