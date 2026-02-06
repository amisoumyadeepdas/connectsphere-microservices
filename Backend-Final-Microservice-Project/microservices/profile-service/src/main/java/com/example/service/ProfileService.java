package com.example.service;

import com.example.cache.ProfileCacheService;
import com.example.config.UserServiceClient;
import com.example.dto.ProfileUpdateRequest;
import com.example.dto.UserResponse;
import com.example.exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserServiceClient userServiceClient;
    private final ProfileCacheService cacheService;


    public UserResponse viewMyProfile(Long userId) {

        // 1️⃣ Try Redis first
        UserResponse cachedUser = cacheService.get(userId);
        if (cachedUser != null && cachedUser.getDateOfBirth() != null) {
            System.out.println("Cache hit!");
            return cachedUser;
        }

        // 2️⃣ Fallback to Feign Client
        else {

            try {
                cachedUser = userServiceClient.getUserById(userId);
            }catch (Exception e){
                throw new ServiceUnavailableException("User service is not responding");
            }

        }

        // 3️⃣ Cache
        cacheService.save(cachedUser);

        return cachedUser;
    }

    public UserResponse updateMyProfile( Long userId, ProfileUpdateRequest request) {
        UserResponse updatedUser = userServiceClient.updateUser(userId, request);
        updatedUser.setRole("USER");
        cacheService.save(updatedUser);
        return updatedUser;
    }
}

