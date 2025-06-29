package com.lingosphinx.profile.dto;

import com.lingosphinx.profile.domain.ProfileLanguage;
import com.lingosphinx.profile.domain.Subscription;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDto {

    private Long id;
    private UUID userId;
    private String name;
    private Subscription subscription;
    @Builder.Default
    private List<ProfileLanguage> languages = new ArrayList<>();
}
