package com.lingosphinx.profile.repository;

import com.lingosphinx.profile.domain.ProfileLanguage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileLanguageRepository extends JpaRepository<ProfileLanguage, Long> {
}
