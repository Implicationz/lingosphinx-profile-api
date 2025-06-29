package com.lingosphinx.profile.service;

import com.lingosphinx.profile.dto.LanguageDto;

import java.util.List;

public interface LanguageService {
    LanguageDto create(LanguageDto language);
    LanguageDto readById(Long id);
    List<LanguageDto> readAll();
    LanguageDto update(Long id, LanguageDto language);
    void delete(Long id);
}
