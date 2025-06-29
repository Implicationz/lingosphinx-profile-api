package com.lingosphinx.profile.mapper;

import com.lingosphinx.profile.domain.Preferences;
import com.lingosphinx.profile.dto.PreferencesDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PreferencesMapper {
    PreferencesDto toDto(Preferences preferences);
    Preferences toEntity(PreferencesDto preferencesDto);
}