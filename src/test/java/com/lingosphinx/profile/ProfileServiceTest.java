package com.lingosphinx.profile;

import com.lingosphinx.profile.dto.ProfileDto;
import com.lingosphinx.profile.service.ProfileService;
import com.lingosphinx.profile.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class ProfileServiceTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void dbProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    private ProfileDto currentUserProfile;

    @AfterEach
    public void deleteCurrentUserProfile() {
        if(currentUserProfile != null) {
            profileService.delete(currentUserProfile.getId());
        }
    }


    private ProfileDto createProfile(String name) {
        return profileService.create(ProfileDto.builder()
                .userId(UUID.randomUUID())
                .name(name)
                .build());
    }

    @Test
    void create_shouldPersistProfile() {
        var profile = createProfile("Name 1");
        assertNotNull(profile.getId());
        assertNotNull(profile.getUserId());
        assertEquals("Name 1", profile.getName());
    }

    @Test
    void createByCurrentUser_shouldPersistProfileForCurrentUser() {
        currentUserProfile = profileService.createByCurrentUser();
        var profile = currentUserProfile;
        assertNotNull(profile.getId());
        assertNotNull(profile.getUserId());
        assertEquals(userService.getCurrentUserId(), profile.getUserId());
    }

    @Test
    void readById_shouldReturnPersistedProfile() {
        var created = createProfile("Name 2");
        var found = profileService.readById(created.getId());
        assertNotNull(found);
        assertEquals("Name 2", found.getName());
        assertEquals(created.getUserId(), found.getUserId());
    }

    @Test
    void readAll_shouldReturnAllProfiles() {
        createProfile("Name 3");
        createProfile("Name 4");
        var all = profileService.readAll();
        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(p -> "Name 3".equals(p.getName())));
        assertTrue(all.stream().anyMatch(p -> "Name 4".equals(p.getName())));
    }

    @Test
    void update_shouldModifyProfile() {
        var created = createProfile("Name 5");
        var updatedProfile = ProfileDto.builder()
                .id(created.getId())
                .userId(created.getUserId())
                .name("New Name 5")
                .languages(created.getLanguages())
                .build();
        var updated = profileService.update(created.getId(), updatedProfile);
        assertEquals("New Name 5", updated.getName());
    }

    @Test
    void updateByCurrentUser_shouldModifyCurrentUserProfile() {
        currentUserProfile = profileService.createByCurrentUser();
        var created = currentUserProfile;
        var update = ProfileDto.builder()
                .id(created.getId())
                .userId(created.getUserId())
                .name("Aktueller User Neu")
                .languages(created.getLanguages())
                .build();
        var updated = profileService.updateByCurrentUser(update);
        assertEquals("Aktueller User Neu", updated.getName());
        assertEquals(created.getUserId(), updated.getUserId());
    }

    @Test
    void delete_shouldRemoveProfile() {
        var created = createProfile("Name 6");
        profileService.delete(created.getId());
        assertThrows(Exception.class, () -> profileService.readById(created.getId()));
    }

    @Test
    void readByCurrentUser_shouldReturnCurrentUserProfile() {
        currentUserProfile = profileService.createByCurrentUser();
        var current = profileService.readByCurrentUser();
        assertNotNull(current);
        assertEquals(userService.getCurrentUserId(), current.getUserId());
    }
}