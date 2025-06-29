package com.lingosphinx.profile.repository;

import com.lingosphinx.profile.domain.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PreferencesRepository extends JpaRepository<Preferences, Long> {
    Optional<Preferences> findByUserId(UUID userId);
}
