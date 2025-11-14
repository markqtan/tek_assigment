package com.markqtan.weather_api.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.markqtan.weather_api.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
public class AppConfig {
    @Value("${caching.spring.weather.ttl.seconds}")
    private int ttl;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(Constants.WEATHER); 
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(ttl, TimeUnit.SECONDS)
                .maximumSize(100));
        return cacheManager;
    }
}
