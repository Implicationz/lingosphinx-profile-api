package com.lingosphinx.profile.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "language",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code"),
                @UniqueConstraint(columnNames = "name")
        },
        indexes = {
                @Index(columnList = "code"),
                @Index(columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LanguageCode code;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;

    public enum Direction {
        LTR, RTL
    }
}