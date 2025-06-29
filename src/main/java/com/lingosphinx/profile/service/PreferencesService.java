package com.lingosphinx.profile.service;

import com.lingosphinx.profile.dto.PreferencesDto;

import java.util.List;
import java.util.UUID;

public interface PreferencesService {
    PreferencesDto create(PreferencesDto preferences);
    PreferencesDto createByCurrentUser();
    PreferencesDto readById(Long id);
    PreferencesDto readByCurrentUser();
    List<PreferencesDto> readAll();
    PreferencesDto update(Long id, PreferencesDto preferences);
    PreferencesDto updateByCurrentUser(PreferencesDto preferences);
    void delete(Long id);
}
