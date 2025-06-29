package com.lingosphinx.profile.service;

import com.lingosphinx.profile.dto.ProfileDto;

import java.util.List;
import java.util.UUID;

public interface ProfileService {
    ProfileDto create(ProfileDto profile);
    ProfileDto createByCurrentUser();
    ProfileDto readById(Long id);
    ProfileDto readByCurrentUser();
    List<ProfileDto> readAll();
    ProfileDto update(Long id, ProfileDto profile);
    ProfileDto updateByCurrentUser(ProfileDto profile);
    void delete(Long id);
}
