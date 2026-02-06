package com.example.config;

import com.example.dto.LoginResponse;
import com.example.dto.LoginUserRequest;
import com.example.fallback.LoginServiceClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "USER-SERVICES", fallback = LoginServiceClientFallback.class)
public interface LoginServiceClient {

    @PostMapping("/api/users")
    LoginResponse createUser(@RequestBody LoginUserRequest request);

    @GetMapping("/api/users/email/{email}")
    LoginResponse getByEmail(@PathVariable String email);
}

