package com.lingosphinx.profile.service;

import com.lingosphinx.profile.domain.Profile;
import com.lingosphinx.profile.dto.ProfileDto;
import com.lingosphinx.profile.dto.ProfileLanguageDto;
import com.lingosphinx.profile.mapper.ProfileLanguageMapper;
import com.lingosphinx.profile.mapper.ProfileMapper;
import com.lingosphinx.profile.repository.ProfileLanguageRepository;
import com.lingosphinx.profile.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final ProfileLanguageRepository profileLanguageRepository;
    private final ProfileMapper profileMapper;
    private final ProfileLanguageMapper profileLanguageMapper;

    @Override
    public ProfileDto create(ProfileDto profileDto) {
        var profile = profileMapper.toEntity(profileDto);
        var savedProfile = profileRepository.save(profile);
        return profileMapper.toDto(savedProfile);
    }

    @Override
    public ProfileDto createByCurrentUser() {
        var userId = userService.getCurrentUserId();
        var profile = Profile.fromUserId(userId);
        var savedProfile = profileRepository.save(profile);
        return profileMapper.toDto(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileDto readById(Long id) {
        var profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        return profileMapper.toDto(profile);
    }

    @Override
    public ProfileDto readByCurrentUser() {
        var userId = userService.getCurrentUserId();
        var profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        return profileMapper.toDto(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfileDto> readAll() {
        return profileRepository.findAll().stream()
                .map(profileMapper::toDto)
                .toList();
    }

    @Override
    public ProfileDto update(Long id, ProfileDto profileDto) {
        var existingProfile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        profileMapper.updateEntityFromDto(profileDto, existingProfile);
        return profileMapper.toDto(existingProfile);
    }

    @Override
    public ProfileDto updateByCurrentUser(ProfileDto profile) {
        var userId = userService.getCurrentUserId();
        var existingProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        profileMapper.updateEntityFromDto(profile, existingProfile);
        existingProfile.setUserId(userId);
        mergeLanguages(existingProfile, profile.getLanguages());
        return profileMapper.toDto(existingProfile);
    }


    public void mergeLanguages(Profile profile, List<ProfileLanguageDto> languages) {
        var existing = profile.getLanguages();
        existing.clear();
        profileLanguageRepository.flush();

        var toAdd = languages.stream().map(l -> {
            var mapped = profileLanguageMapper.toEntity(l);
            mapped.setId(null);
            mapped.setProfile(profile);
            return mapped;
        }).toList();
        existing.addAll(toAdd);
    }

    @Override
    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}