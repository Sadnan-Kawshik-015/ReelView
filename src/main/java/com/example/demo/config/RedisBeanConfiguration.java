package com.example.demo.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
public class RedisBeanConfiguration {
    private final String _hostname;
    private final int _port;
    private final int _database;
    private final String _password;
    private final long _timeout;
    @Value("${app.refreshTokenExpirationMinutes}")
    private long refreshTokenTTL;

    public RedisBeanConfiguration(
            @Value("${spring.data.redis.host}") final String hostname,
            @Value("${spring.data.redis.port}") final int port,
            @Value("${spring.data.redis.database}") final int database,
            @Value("${spring.data.redis.password}") final String password,
            @Value("${spring.data.redis.timeout}") final long timeout
    ) {

        this._hostname = hostname;
        this._port = port;
        this._database = database;
        this._password = password;
        this._timeout = timeout;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(_hostname);
        config.setPort(_port);
        config.setPassword(_password);
        config.setDatabase(_database);

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(_timeout))
                .build();

        return new LettuceConnectionFactory(config, clientConfig);
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(
            @Qualifier("redisConnectionFactory") RedisConnectionFactory redisConnectionFactory
    ) {

        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
    @Bean(name = "refreshTokenCacheManager")
    @Primary
    public CacheManager refreshTokenCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(refreshTokenTTL))
                .disableCachingNullValues()
                .disableKeyPrefix()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new Jackson2JsonRedisSerializer<Object>(Object.class)))
                .disableCachingNullValues();
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

}
