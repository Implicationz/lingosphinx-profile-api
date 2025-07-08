package com.lingosphinx.profile.mapper;

import com.lingosphinx.profile.domain.ProfileLanguage;
import com.lingosphinx.profile.dto.ProfileLanguageDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProfileLanguageMapper {
    ProfileLanguageDto toDto(ProfileLanguage profileLanguage);
    ProfileLanguage toEntity(ProfileLanguageDto profileLanguageDto);

    void updateEntityFromDto(ProfileLanguageDto profileLanguageDto, @MappingTarget  ProfileLanguage existingProfileLanguage);
}