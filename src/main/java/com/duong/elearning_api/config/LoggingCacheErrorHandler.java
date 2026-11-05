package com.duong.elearning_api.config;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

@Slf4j(topic = "CACHE-ERROR-HANDLER")
public class LoggingCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.warn("Cache GET error [{}:{}]: {}", cache.getName(), key, exception.getMessage());
    }

    @Override
    public void handleCachePutError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key, Object value) {
        log.warn("Cache PUT error [{}:{}]: {}", cache.getName(), key, exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(@NonNull RuntimeException exception, @NonNull Cache cache, @NonNull Object key) {
        log.warn("Cache EVICT error [{}:{}]: {}", cache.getName(), key, exception.getMessage());
    }

    @Override
    public void handleCacheClearError(@NonNull RuntimeException exception, @NonNull Cache cache) {
        log.warn("Cache CLEAR error [{}]: {}", cache.getName(), exception.getMessage());
    }
}
