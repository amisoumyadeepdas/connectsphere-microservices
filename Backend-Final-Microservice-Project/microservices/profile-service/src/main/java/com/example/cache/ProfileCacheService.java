package com.example.cache;

//import com.example.dto.LoginResponse;
import com.example.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileCacheService {

    private final RedisTemplate<String, UserResponse> redisTemplate;

    private static final String KEY = "LOGIN_USER";

    public void save(UserResponse user) {
        redisTemplate.opsForHash()
                .put(KEY, user.getId().toString(), user);
    }

    public UserResponse get(Long id) {
        return (UserResponse) redisTemplate.opsForHash()
                .get(KEY, id.toString());
    }
}

