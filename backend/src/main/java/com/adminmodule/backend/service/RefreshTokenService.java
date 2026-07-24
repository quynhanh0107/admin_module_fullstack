package com.adminmodule.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
//import java.util.concurrent.TimeUnit;
import java.time.Duration;

@Service
public class RefreshTokenService {

    @Autowired
    private StringRedisTemplate redisTemp;

    public String createRefreshToken(String username) {
        String refreshToken = UUID.randomUUID().toString();
        redisTemp.opsForValue().set(refreshToken, username, Duration.ofDays(7));

        return refreshToken;
    }

    public String getRefreshToken(String refreshToken) {
        return redisTemp.opsForValue().get(refreshToken);
    }

    public void deleteRefreshToken(String refreshToken) {
        redisTemp.delete(refreshToken);
    }
    
}
