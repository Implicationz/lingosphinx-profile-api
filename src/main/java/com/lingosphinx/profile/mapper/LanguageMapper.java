package com.lingosphinx.profile.mapper;

import com.lingosphinx.profile.domain.Language;
import com.lingosphinx.profile.dto.LanguageDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface LanguageMapper {
    LanguageDto toDto(Language language);
    Language toEntity(LanguageDto languageDto);

    void updateEntityFromDto(LanguageDto languageDto, @MappingTarget  Language existingLanguage);
}