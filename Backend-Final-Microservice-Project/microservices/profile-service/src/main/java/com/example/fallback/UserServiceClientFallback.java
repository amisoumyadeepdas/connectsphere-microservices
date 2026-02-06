package com.example.fallback;

import com.example.config.UserServiceClient;
import com.example.dto.ProfileUpdateRequest;
import com.example.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserResponse getUserById(Long id) {
        log.warn("Fallback triggered for finding User with Id: {}", id);

        throw new RuntimeException("USER Service not responding");
    }

    @Override
    public UserResponse updateUser(Long id, ProfileUpdateRequest request) {
        log.warn("Fallback triggered for updateUser with Id: {}", id);

        throw new RuntimeException("USER Service not responding");
    }
}
