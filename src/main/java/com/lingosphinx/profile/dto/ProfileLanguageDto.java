package com.lingosphinx.profile.dto;

import com.lingosphinx.profile.domain.Language;
import com.lingosphinx.profile.domain.Profile;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileLanguageDto {

    private Long id;
    private Profile profile;
    @Builder.Default
    private int position = 0;
    private Language language;
}