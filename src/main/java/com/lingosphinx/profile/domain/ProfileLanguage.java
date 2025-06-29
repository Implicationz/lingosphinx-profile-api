package com.lingosphinx.profile.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "profile_language",
        uniqueConstraints = @UniqueConstraint(columnNames = {"profile_id", "position"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false)
    private int position;

    @ManyToOne(optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;
}