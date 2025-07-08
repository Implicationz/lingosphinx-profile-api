package com.lingosphinx.profile.repository;

import com.lingosphinx.profile.domain.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @EntityGraph(attributePaths = { "subscription", "languages", "languages.language" })
    Optional<Profile> findByUserId(UUID userId);
}
