package com.example.config;

import com.example.entity.User;
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
    public RedisTemplate<String, User> redisTemplate() {

        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());

        // Key serializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // Value serializer (JSON)
        JacksonJsonRedisSerializer<User> serializer = new JacksonJsonRedisSerializer<>(User.class);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
