package com.example.controller;

import com.example.dto.ProfileUpdateRequest;
import com.example.dto.UserResponse;
import com.example.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Headers are sent directly by login-service / frontend
     * NO JWT
     * NO forwarding
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getMyProfile( @PathVariable Long userId ) {

//        if (!"USER".equals(role)) {
//            throw new RuntimeException("Access denied");
//        }

        return ResponseEntity.ok(profileService.viewMyProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateMyProfile(
            @PathVariable Long userId,
            @RequestBody ProfileUpdateRequest request) {

//        if (!"USER".equals(role)) {
//            throw new RuntimeException("Access denied");
//        }

        return ResponseEntity.ok(profileService.updateMyProfile(userId, request));
    }
}

