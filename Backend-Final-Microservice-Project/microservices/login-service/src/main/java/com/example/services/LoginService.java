package com.example.services;

import com.example.cache.LoginCacheService;
import com.example.config.LoginServiceClient;
import com.example.dto.LoginCredential;
import com.example.dto.LoginUserRequest;
import com.example.dto.LoginResponse;
import com.example.exception.ServiceUnavailableException;
import com.example.exception.UserNotFoundException;
import com.example.kafka_producer.LoginEventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final LoginServiceClient userClient;
    private final LoginCacheService cacheService;
    private final LoginEventProducer eventProducer;

    public LoginResponse login(LoginCredential request) {

        try {

            // 1️⃣ Find user
            LoginResponse user = userClient.getByEmail(request.getEmail());
            if(user == null){
                throw new UserNotFoundException(request.getEmail());
            }

            // 2️⃣ Decide role
            String role = request.getEmail().endsWith("@admin.com")
                    ? "ADMIN"
                    : "USER";
            user.setRole(role);

            // 3️⃣ Cache
            cacheService.save(user);


            // 4️⃣ Kafka
            if ("ADMIN".equals(role)) {
                eventProducer.adminLoggedIn(user);
            } else {
                eventProducer.userLoggedIn(user);
            }

            return user;

        } catch (UserNotFoundException e) {
            // This comes from fallback when user is not found (404)
            log.warn("Login failed: User not found for email: {}", request.getEmail());
            throw new RuntimeException("User not found. Please register first.");

        } catch (ServiceUnavailableException e) {
            // This comes from fallback when service is down
            log.error("Login failed: User service unavailable");
            throw new RuntimeException("Service temporarily unavailable. Please try again later.");

        } catch (Exception e) {
            // Catch any other exceptions
            log.error("Login failed with unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException("Login failed. Please try again.");
        }
    }



    // ✅ REGISTER METHOD
    public LoginResponse register(LoginUserRequest request) {

        try {
            // 1️⃣ Create user in USER-SERVICE
            LoginResponse user = userClient.createUser(request);

            // 2️⃣ Assign role
            user.setRole("USER");

            // 3️⃣ Cache
            cacheService.save(user);

            // 4️⃣ Kafka
            eventProducer.userRegistered(user);

            return user;

        } catch (ServiceUnavailableException e) {
            log.error("Registration failed: User service unavailable");
            throw new RuntimeException("Registration service is temporarily unavailable. Please try again later.");

        } catch (Exception e) {
            log.error("Registration failed: {}", e.getMessage(), e);
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
}