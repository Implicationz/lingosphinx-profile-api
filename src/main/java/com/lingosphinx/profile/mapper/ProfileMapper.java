package com.lingosphinx.profile.mapper;

import com.lingosphinx.profile.domain.Profile;
import com.lingosphinx.profile.dto.ProfileDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ProfileMapper {
    ProfileDto toDto(Profile profile);
    Profile toEntity(ProfileDto profileDto);

    void updateEntityFromDto(ProfileDto profileDto, @MappingTarget  Profile existingProfile);
}