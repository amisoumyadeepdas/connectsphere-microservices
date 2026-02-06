package com.example.cache;

import com.example.dto.LoginResponse;
//import com.example.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginCacheService {

    private final RedisTemplate<String, LoginResponse> redisTemplate;

    private static final String KEY = "LOGIN_USER";

    public void save(LoginResponse user) {
        redisTemplate.opsForHash()
                .put(KEY, user.getId().toString(), user);
    }

    public LoginResponse get(Long id) {
        return (LoginResponse) redisTemplate.opsForHash()
                .get(KEY, id.toString());
    }
}

