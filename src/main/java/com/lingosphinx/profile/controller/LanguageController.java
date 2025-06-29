package com.lingosphinx.profile.controller;

import com.lingosphinx.profile.dto.LanguageDto;
import com.lingosphinx.profile.service.LanguageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/language")
@RequiredArgsConstructor
@Tag(name = "Language API")
public class LanguageController {

    private final LanguageService languageService;

    @PostMapping
    public ResponseEntity<LanguageDto> create(@RequestBody @Valid LanguageDto language) {
        return ResponseEntity.status(HttpStatus.CREATED).body(languageService.create(language));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDto> readById(@PathVariable Long id) {
        return ResponseEntity.ok(languageService.readById(id));
    }

    @GetMapping
    public ResponseEntity<List<LanguageDto>> readAll() {
        return ResponseEntity.ok(languageService.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<LanguageDto> update(@PathVariable Long id, @RequestBody @Valid LanguageDto language) {
        return ResponseEntity.ok(languageService.update(id, language));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        languageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}