package com.lingosphinx.profile.repository;

import com.lingosphinx.profile.domain.Preferences;
import com.lingosphinx.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserId(UUID userId);
}
