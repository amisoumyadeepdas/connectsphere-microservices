package com.example.config;

import com.example.dto.ProfileUpdateRequest;
import com.example.dto.UserResponse;
import com.example.fallback.UserServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "USER-SERVICES", fallback = UserServiceClientFallback.class)
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable Long id);

    @PutMapping("/api/users/{id}")
    UserResponse updateUser( @PathVariable Long id, @RequestBody ProfileUpdateRequest request );
}

