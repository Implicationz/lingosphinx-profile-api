package com.lingosphinx.profile.service;

import com.lingosphinx.profile.domain.Profile;
import com.lingosphinx.profile.dto.ProfileDto;
import com.lingosphinx.profile.mapper.ProfileMapper;
import com.lingosphinx.profile.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final UserService userService;
    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

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
        var savedProfile = profileRepository.save(existingProfile);
        return profileMapper.toDto(savedProfile);
    }

    @Override
    public ProfileDto updateByCurrentUser(ProfileDto profile) {
        var userId = userService.getCurrentUserId();
        var existingProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        profileMapper.updateEntityFromDto(profile, existingProfile);
        var savedProfile = profileRepository.save(existingProfile);
        return profileMapper.toDto(savedProfile);
    }

    @Override
    public void delete(Long id) {
        profileRepository.deleteById(id);
    }
}