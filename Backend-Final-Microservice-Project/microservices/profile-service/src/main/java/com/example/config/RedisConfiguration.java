package com.example.config;

//import com.example.dto.LoginResponse;
import com.example.dto.UserResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

    // Jedis Connection Factory
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory redis = new JedisConnectionFactory();
        return redis;
    }

    // RedisTemplate with JSON serialization
    @Bean
    public RedisTemplate<String, UserResponse> redisTemplate() {

        RedisTemplate<String, UserResponse> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        // Key serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Value serializer (JSON)
        JacksonJsonRedisSerializer<UserResponse> serializer = new JacksonJsonRedisSerializer<>(UserResponse.class);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
