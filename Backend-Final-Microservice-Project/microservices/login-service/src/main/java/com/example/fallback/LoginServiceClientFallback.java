package com.example.fallback;

import com.example.config.LoginServiceClient;
import com.example.dto.LoginUserRequest;
import com.example.dto.LoginResponse;
import com.example.exception.ServiceUnavailableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginServiceClientFallback implements LoginServiceClient {

    @Override
    public LoginResponse createUser(LoginUserRequest request) {
        log.warn("Fallback triggered for createUser with email: {}", request.getEmail());

        // Default fallback for circuit breaker/open circuit
        throw new ServiceUnavailableException("User service is currently unavailable. Please try again later.");
    }

    @Override
    public LoginResponse getByEmail(String email) {
        log.warn("Fallback triggered for getByEmail with email: {}", email);

        // Default fallback (circuit open, timeout, etc.)
        throw new ServiceUnavailableException("User service is currently unavailable. Please try again later.");
    }
}















//package com.example.fallback;
//
//import com.example.config.LoginServiceClient;
//import com.example.dto.LoginResponse;
//import com.example.dto.LoginUserRequest;
//import org.springframework.stereotype.Component;
//
//@Component
//public class LoginServiceClientFallback implements LoginServiceClient {
//
//    @Override
//    public LoginResponse createUser(LoginUserRequest request) {
//        return LoginResponse.builder()
//                .id(0L)
//                .email(request.getEmail())
//                .role("UNKNOWN")
//                .name(null)
//                .city(null)
//                .company(null)
//                .gender(null)
//                .dateOfBirth(null)
//                .build();
//    }
//
//    @Override
//    public LoginResponse getByEmail(String email) {
//        return LoginResponse.builder()
//                .id(0L)
//                .email(email)
//                .role("UNKNOWN")
//                .name(null)
//                .city(null)
//                .company(null)
//                .gender(null)
//                .dateOfBirth(null)
//                .build();
//    }
//}
