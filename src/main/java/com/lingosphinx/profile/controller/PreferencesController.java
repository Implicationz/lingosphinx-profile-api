package com.lingosphinx.profile.controller;


import com.lingosphinx.profile.dto.PreferencesDto;
import com.lingosphinx.profile.service.PreferencesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
@Tag(name = "Preferences API")
public class PreferencesController {

    private final PreferencesService preferencesService;

    @PostMapping
    public ResponseEntity<PreferencesDto> create(@RequestBody @Valid PreferencesDto preferences) {
        return ResponseEntity.status(HttpStatus.CREATED).body(preferencesService.create(preferences));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreferencesDto> readById(@PathVariable Long id) {
        return ResponseEntity.ok(preferencesService.readById(id));
    }

    @GetMapping
    public ResponseEntity<List<PreferencesDto>> readAll() {
        return ResponseEntity.ok(preferencesService.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreferencesDto> update(@PathVariable Long id, @RequestBody @Valid PreferencesDto preferences) {
        return ResponseEntity.ok(preferencesService.update(id, preferences));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        preferencesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}