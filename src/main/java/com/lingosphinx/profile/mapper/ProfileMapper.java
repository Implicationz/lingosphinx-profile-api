package com.lingosphinx.profile.mapper;

import com.lingosphinx.profile.domain.Profile;
import com.lingosphinx.profile.domain.ProfileLanguage;
import com.lingosphinx.profile.dto.ProfileDto;
import com.lingosphinx.profile.dto.ProfileLanguageDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProfileMapper {

    ProfileDto toDto(Profile profile);
    Profile toEntity(ProfileDto profileDto);

    @Mapping(target = "profile", ignore = true)
    ProfileLanguageDto toDto(ProfileLanguage language);

    @Mapping(target = "profile", ignore = true)
    ProfileLanguage toEntity(ProfileLanguageDto language);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscription", ignore = true)
    @Mapping(target = "languages", ignore = true)
    void updateEntityFromDto(ProfileDto profileDto, @MappingTarget Profile existingProfile);
}