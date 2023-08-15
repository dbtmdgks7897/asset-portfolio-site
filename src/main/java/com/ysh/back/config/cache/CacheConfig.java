package com.ysh.back.config.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CacheConfig {

    // @Bean
    // public CacheManager cacheManager() {
    //     CaffeineCacheManager cacheManager = new CaffeineCacheManager("accessTokenCache");
    //     cacheManager.setCaffeine(Caffeine.newBuilder()
    //             .maximumSize(100)
    //             .expireAfterWrite(1440, TimeUnit.MINUTES));
        
    //     return cacheManager;
    // }
     @Bean()
     @Primary
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .recordStats());

        return cacheManager;
    }
    
    @Bean("approvalKeyCacheManager")
    public CacheManager approvalKeyCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("approvalKeyCache");

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(200)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats());

        return cacheManager;
    }
}
