package com.sixmycat.catchy.config;

import com.sixmycat.catchy.feature.auth.command.domain.aggregate.RefreshToken;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password:}") // 비밀번호 없을 시 빈 값 허용
    private String redisPassword;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisHost);
        redisConfig.setPort(redisPort);

        if (!redisPassword.isBlank()) {
            redisConfig.setPassword(RedisPassword.of(redisPassword));
        }

        return new LettuceConnectionFactory(redisConfig);
    }

    // RefreshToken 전용 RedisTemplate (이름 명확화)
    @Bean(name = "refreshTokenRedisTemplate")
    public RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, RefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key를 String으로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); // value를 JSON으로 직렬화
        return redisTemplate;
    }

    @Bean(name = "tempMemberRedisTemplate")
    public RedisTemplate<String, TempMember> tempMemberRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, TempMember> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }
}
