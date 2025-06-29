package com.lingosphinx.profile.service;


import com.lingosphinx.profile.domain.Preferences;
import com.lingosphinx.profile.dto.PreferencesDto;
import com.lingosphinx.profile.mapper.PreferencesMapper;
import com.lingosphinx.profile.repository.PreferencesRepository;
import com.lingosphinx.profile.service.PreferencesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PreferencesServiceImpl implements PreferencesService {

    private final UserService userService;
    private final PreferencesRepository preferencesRepository;
    private final PreferencesMapper preferencesMapper;

    @Override
    public PreferencesDto create(PreferencesDto preferencesDto) {
        var preferences = preferencesMapper.toEntity(preferencesDto);
        var savedPreferences = preferencesRepository.save(preferences);
        return preferencesMapper.toDto(savedPreferences);
    }

    @Override
    public PreferencesDto createByCurrentUser() {
        var userId = userService.getCurrentUserId();
        var preferences = Preferences.fromUser(userId);
        var savedPreferences = preferencesRepository.save(preferences);
        return preferencesMapper.toDto(savedPreferences);
    }

    @Override
    @Transactional(readOnly = true)
    public PreferencesDto readById(Long id) {
        var preferences = preferencesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Preferences not found"));
        return preferencesMapper.toDto(preferences);
    }

    @Override
    public PreferencesDto readByCurrentUser() {
        var userId = userService.getCurrentUserId();
        var preferences = preferencesRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Preferences not found"));
        return preferencesMapper.toDto(preferences);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreferencesDto> readAll() {
        return preferencesRepository.findAll().stream()
                .map(preferencesMapper::toDto)
                .toList();
    }

    @Override
    public PreferencesDto update(Long id, PreferencesDto preferencesDto) {
        var existingPreferences = preferencesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Preferences not found"));

        var savedPreferences = preferencesRepository.save(existingPreferences);
        return preferencesMapper.toDto(savedPreferences);
    }

    @Override
    public PreferencesDto updateByCurrentUser(PreferencesDto preferences) {
        return null;
    }

    @Override
    public void delete(Long id) {
        preferencesRepository.deleteById(id);
    }
}