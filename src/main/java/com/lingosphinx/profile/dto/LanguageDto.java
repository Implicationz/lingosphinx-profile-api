package com.lingosphinx.profile.dto;

import com.lingosphinx.profile.domain.Language;
import com.lingosphinx.profile.domain.LanguageCode;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LanguageDto {

    private Long id;
    private LanguageCode code;
    private String name;

    private Language.Direction direction;
}