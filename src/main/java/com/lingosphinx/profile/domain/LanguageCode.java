package com.lingosphinx.profile.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonSerialize(using = LanguageCodeSerializer.class)
@JsonDeserialize(using = LanguageCodeDeserializer.class)
@EqualsAndHashCode
public class LanguageCode {
    public static final LanguageCode ENGLISH = LanguageCode.builder().value("english").build();
    String value;

    public static LanguageCode valueOf(String value) {
        return new LanguageCode(value);
    }
}

