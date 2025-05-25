package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class TempMemberRedisServiceImpl implements TempMemberRedisService {

    private final RedisTemplate<String, TempMember> redisTemplate;

    @Override
    public void save(TempMember member) {
        String key = switch (member.getSocial().toUpperCase()) {
            case "KAKAO" -> "TEMP_K_MEMBER:" + member.getEmail();
            case "NAVER" -> "TEMP_N_MEMBER:" + member.getEmail();
            case "GOOGLE" -> "TEMP_G_MEMBER:" + member.getEmail();
            default -> throw new BusinessException(ErrorCode.SOCIAL_PLATFORM_NOT_SUPPORTED);
        };
        redisTemplate.opsForValue().set(key, member, Duration.ofMinutes(10));
    }
}
