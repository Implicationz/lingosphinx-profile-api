package com.lingosphinx.profile.service;


import com.lingosphinx.profile.domain.Language;
import com.lingosphinx.profile.dto.LanguageDto;
import com.lingosphinx.profile.mapper.LanguageMapper;
import com.lingosphinx.profile.repository.LanguageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    @Override
    public LanguageDto create(LanguageDto languageDto) {
        var language = languageMapper.toEntity(languageDto);
        var savedLanguage = languageRepository.save(language);
        return languageMapper.toDto(savedLanguage);
    }

    @Override
    @Transactional(readOnly = true)
    public LanguageDto readById(Long id) {
        var language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found"));
        return languageMapper.toDto(language);
    }


    @Override
    @Transactional(readOnly = true)
    public List<LanguageDto> readAll() {
        return languageRepository.findAll().stream()
                .map(languageMapper::toDto)
                .toList();
    }

    @Override
    public LanguageDto update(Long id, LanguageDto languageDto) {
        var existingLanguage = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found"));
        languageMapper.updateEntityFromDto(languageDto, existingLanguage);
        var savedLanguage = languageRepository.save(existingLanguage);
        return languageMapper.toDto(savedLanguage);
    }

    @Override
    public void delete(Long id) {
        languageRepository.deleteById(id);
    }
}