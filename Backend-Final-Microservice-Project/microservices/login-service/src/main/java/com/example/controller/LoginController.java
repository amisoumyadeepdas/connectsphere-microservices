package com.example.controller;

import com.example.dto.LoginCredential;
import com.example.dto.LoginResponse;
import com.example.dto.LoginUserRequest;
import com.example.services.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginCredential request) {
        System.out.println("Login request received: " + request);
        return ResponseEntity.ok(loginService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(
            @RequestBody LoginUserRequest request) {

        return ResponseEntity.ok(loginService.register(request));
    }
}


