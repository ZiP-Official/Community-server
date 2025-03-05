package com.zip.community.common.config.redis;

import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.*;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    @Primary
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfiguration())
                .build();
    }

    private RedisCacheConfiguration defaultCacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(Duration.ofHours(1L))
                .disableCachingNullValues();
    }

    private RedisCacheConfiguration listCacheConfiguration() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return RedisCacheConfiguration
                .defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(5L))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))
                );
    }

    @Bean
    RedisTemplate<String, Long> StringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String,Long> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer(Long.class));
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer(Long.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<Long, Long> LongtypeRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        // RedisTemplate의 제네릭 타입을 명시적으로 지정
        final RedisTemplate<Long, Long> redisTemplate = new RedisTemplate<>();

        // 연결 팩토리 설정
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 키와 값 직렬화 방식 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());  // 키는 문자열로 직렬화
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));  // 값은 Long으로 직렬화
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());  // 해시 키 직렬화 방식
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(Long.class));  // 해시 값 직렬화 방식

        return redisTemplate;
    }

}
