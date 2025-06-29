package com.lingosphinx.profile.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import jakarta.persistence.Convert;
import org.hibernate.annotations.JdbcTypeCode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.type.SqlTypes;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    @Builder.Default
    private Map<String, Object> attributes = new HashMap<>();

    public static Preferences fromUser(UUID userId) {
        return Preferences.builder()
                .userId(userId)
                .build();
    }
}