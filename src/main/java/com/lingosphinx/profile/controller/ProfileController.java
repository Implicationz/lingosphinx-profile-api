package com.lingosphinx.profile.controller;

import com.lingosphinx.profile.dto.ProfileDto;
import com.lingosphinx.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@Tag(name = "Profile API")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping
    public ResponseEntity<ProfileDto> create(@RequestBody @Valid ProfileDto profile) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.create(profile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> readById(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.readById(id));
    }

    @PostMapping("/current")
    public ResponseEntity<ProfileDto> createByCurrentUser(@RequestBody @Valid ProfileDto profile) {
        return ResponseEntity.ok(profileService.createByCurrentUser());
    }

    @PutMapping("/current")
    public ResponseEntity<ProfileDto> updateByCurrentUser(@RequestBody @Valid ProfileDto profile) {
        return ResponseEntity.ok(profileService.updateByCurrentUser(profile));
    }

    @GetMapping("/current")
    public ResponseEntity<ProfileDto> readByCurrentUser() {
        return ResponseEntity.ok(profileService.readByCurrentUser());
    }

    @GetMapping
    public ResponseEntity<List<ProfileDto>> readAll() {
        return ResponseEntity.ok(profileService.readAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> update(@PathVariable Long id, @RequestBody @Valid ProfileDto profile) {
        return ResponseEntity.ok(profileService.update(id, profile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}