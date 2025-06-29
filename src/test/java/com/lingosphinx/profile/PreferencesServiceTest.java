package com.lingosphinx.profile;

import com.lingosphinx.profile.dto.PreferencesDto;
import com.lingosphinx.profile.service.PreferencesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class PreferencesServiceTest {

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
    private PreferencesService preferencesService;

    private PreferencesDto createPreferencesDto(UUID userId) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("theme", "dark");
        attributes.put("notifications", true);
        return PreferencesDto.builder()
                .userId(userId)
                .attributes(attributes)
                .build();
    }

    @Test
    void createAndReadById_shouldPersistAndReturnPreferences() {
        UUID userId = UUID.randomUUID();
        PreferencesDto dto = createPreferencesDto(userId);
        PreferencesDto saved = preferencesService.create(dto);

        assertNotNull(saved.getId());
        assertEquals(userId, saved.getUserId());
        assertEquals("dark", saved.getAttributes().get("theme"));

        PreferencesDto found = preferencesService.readById(saved.getId());
        assertEquals(saved.getId(), found.getId());
        assertEquals("dark", found.getAttributes().get("theme"));
    }

    @Test
    void readAll_shouldReturnAllPreferences() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        preferencesService.create(createPreferencesDto(userId1));
        preferencesService.create(createPreferencesDto(userId2));

        List<PreferencesDto> all = preferencesService.readAll();
        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(p -> p.getUserId().equals(userId1)));
        assertTrue(all.stream().anyMatch(p -> p.getUserId().equals(userId2)));
    }

    @Test
    void update_shouldModifyPreferences() {
        UUID userId = UUID.randomUUID();
        PreferencesDto dto = createPreferencesDto(userId);
        PreferencesDto saved = preferencesService.create(dto);

        saved.getAttributes().put("theme", "light");
        PreferencesDto updated = preferencesService.update(saved.getId(), saved);

        assertEquals("light", updated.getAttributes().get("theme"));
    }

    @Test
    void delete_shouldRemovePreferences() {
        UUID userId = UUID.randomUUID();
        PreferencesDto dto = createPreferencesDto(userId);
        PreferencesDto saved = preferencesService.create(dto);

        preferencesService.delete(saved.getId());
        assertThrows(Exception.class, () -> preferencesService.readById(saved.getId()));
    }
}