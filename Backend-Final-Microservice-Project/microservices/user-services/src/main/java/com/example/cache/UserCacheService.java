package com.example.cache;

import com.example.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final RedisTemplate<String, User> redisTemplate;

    private static final String KEY = "USER";

    public void save(User user) {
        redisTemplate.opsForHash()
                .put(KEY, user.getId().toString(), user);
    }

    public User get(Long id) {
        return (User) redisTemplate.opsForHash()
                .get(KEY, id.toString());
    }

    public void delete(Long id) {
        redisTemplate.opsForHash()
                .delete(KEY, id.toString());
    }
}
