package com.lingosphinx.profile.dto;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PreferencesDto {

    private Long id;
    private UUID userId;
    private Map<String, Object> attributes;
}