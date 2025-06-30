package com.lingosphinx.profile;

import com.lingosphinx.profile.dto.LanguageDto;
import com.lingosphinx.profile.domain.Language;
import com.lingosphinx.profile.domain.LanguageCode;
import com.lingosphinx.profile.service.LanguageService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class LanguageServiceTest {

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
    private LanguageService languageService;

    private LanguageDto createLanguageDto() {
        return LanguageDto.builder()
                .code(LanguageCode.valueOf("EN"))
                .name("English")
                .direction(Language.Direction.LTR)
                .build();
    }

    @Test
    void createAndReadById_shouldPersistAndReturnLanguage() {
        var dto = LanguageDto.builder()
                .code(LanguageCode.valueOf("EN"))
                .name("English")
                .direction(Language.Direction.LTR)
                .build();
        var saved = languageService.create(dto);

        assertNotNull(saved.getId());
        assertEquals(LanguageCode.valueOf("EN"), saved.getCode());
        assertEquals("English", saved.getName());

        var found = languageService.readById(saved.getId());
        assertEquals(saved.getId(), found.getId());
        assertEquals("English", found.getName());
    }

    @Test
    void readAll_shouldReturnAllLanguages() {
        languageService.create(LanguageDto.builder()
                .code(LanguageCode.valueOf("RU"))
                .name("Russian")
                .direction(Language.Direction.LTR)
                .build());
        languageService.create(LanguageDto.builder()
                .code(LanguageCode.valueOf("DE"))
                .name("Deutsch")
                .direction(Language.Direction.LTR)
                .build());

        List<LanguageDto> all = languageService.readAll();
        assertTrue(all.size() >= 2);
        assertTrue(all.stream().anyMatch(l -> "Russian".equals(l.getName())));
        assertTrue(all.stream().anyMatch(l -> "Deutsch".equals(l.getName())));
    }

    @Test
    void update_shouldModifyLanguage() {
        var dto = LanguageDto.builder()
                .code(LanguageCode.valueOf("FR"))
                .name("Französisch")
                .direction(Language.Direction.LTR)
                .build();
        var saved = languageService.create(dto);

        var update = LanguageDto.builder()
                .id(saved.getId())
                .code(saved.getCode())
                .name("Französisch Neu")
                .direction(saved.getDirection())
                .build();

        var updated = languageService.update(saved.getId(), update);
        assertEquals("Französisch Neu", updated.getName());
    }

    @Test
    void delete_shouldRemoveLanguage() {
        var dto = LanguageDto.builder()
                .code(LanguageCode.valueOf("IT"))
                .name("Italienisch")
                .direction(Language.Direction.LTR)
                .build();
        var saved = languageService.create(dto);

        languageService.delete(saved.getId());
        assertThrows(Exception.class, () -> languageService.readById(saved.getId()));
    }
}