package com.example.demo.repositories;

import com.example.demo.dtos.helper.TokenDTO;
import com.example.demo.utils.constants.Cache;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenDaoImpl implements RefreshTokenDao {
    @Autowired
    @Qualifier("refreshTokenCacheManager")
    private CacheManager refreshTokenCacheManager;
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${app.previousRefreshTokenTTLSeconds}")
    private long previousRefreshTokenTTL;

    public RefreshTokenDaoImpl(@Qualifier("stringRedisTemplate") StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    @Override
    public String getRefreshToken(String encrypted_token) {
        String key = DigestUtils.sha256Hex(encrypted_token);
        return stringRedisTemplate.opsForValue()
                .get(key);
    }
    @Override
    public boolean delete(String encrypted_refresh_token) {
        if (encrypted_refresh_token != null) {
            var cache = refreshTokenCacheManager.getCache(Cache.cacheName);
            assert cache != null;
            return cache.evictIfPresent(DigestUtils.sha256Hex(encrypted_refresh_token));
        }
        return false;
    }

    @Override
    public boolean save(String encrypted_refresh_token, String user_id) {

        String sha2digest = DigestUtils.sha256Hex(encrypted_refresh_token);
        TokenDTO tokenDTO = TokenDTO.builder()
                .user_id(user_id)
                .token_type("refresh_token")
                .build();
        var cache = refreshTokenCacheManager.getCache(Cache.cacheName);
        assert cache != null;
        cache.put(sha2digest, tokenDTO);
        return true;
    }
    // update the TTL of previous token instead of delete
    @Override
    public boolean updatePreviousRefreshToken(String encryptedRefreshToken) {
        if (encryptedRefreshToken == null || stringRedisTemplate == null) {
            return false;
        }
        try {
            String key = DigestUtils.sha256Hex(encryptedRefreshToken);
            Boolean exists = stringRedisTemplate.hasKey(key);
            if (exists == null || !exists) {
                return false;
            }
            Long ttl = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (ttl == null || ttl <= 0) {
                // Already expired or no TTL
                return false;
            } else if (ttl > previousRefreshTokenTTL) {
                // updating the ttl to 5 seconds so that it will delete after 5 seconds.
                return Boolean.TRUE.equals(stringRedisTemplate.expire(key, previousRefreshTokenTTL, TimeUnit.SECONDS));
            }
            // TTL is already ≤ 5s
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
