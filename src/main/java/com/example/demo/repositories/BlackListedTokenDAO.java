package com.example.demo.repositories;

import com.example.demo.dtos.helper.TokenDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class BlackListedTokenDAO {
    private final StringRedisTemplate stringRedisTemplate;

    public BlackListedTokenDAO(@Qualifier("stringRedisTemplate") StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    public void setTokenBlacklisted(String user_id, String encrypted_auth_token, Date expire_at) throws JsonProcessingException {
        String sha2digest = DigestUtils.sha256Hex(encrypted_auth_token);
        stringRedisTemplate.opsForValue()
                .set(sha2digest, new ObjectMapper().writeValueAsString(TokenDTO.builder()
                        .user_id(user_id)
                        .token_type("blacklisted_token")
                        .build()));
        stringRedisTemplate.expireAt(sha2digest, expire_at);
    }
}
