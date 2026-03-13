package com.mar.config;

import com.mar.api.dto.GameInfoResponse;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.core.Ehcache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

    @Value("${cache.session.user.size}")
    private Integer userSessionCacheSize;

    @Bean
    public CacheManager getEhcacheManager() {
        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build();
        cacheManager.init();
        return cacheManager;
    }

    @Bean
    public Ehcache<String, GameInfoResponse> getFileNameCache(@Autowired CacheManager cacheManager) {
        return (Ehcache<String, GameInfoResponse>) cacheManager
                .createCache("users_session_cache", CacheConfigurationBuilder
                        .newCacheConfigurationBuilder(
                                String.class, GameInfoResponse.class,
                                ResourcePoolsBuilder.heap(userSessionCacheSize)
                        )
                );
    }

}
