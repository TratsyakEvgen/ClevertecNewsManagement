package ru.clevertec.news.config;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RequiredArgsConstructor
public class AlgorithmCacheManager implements CacheManager {
    private final String algorithm;
    private final int capacity;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private final Map<String, CacheSupplier<Integer, String, Cache>> algorithmCahceSeupplierMap = new HashMap<>();

    {
        algorithmCahceSeupplierMap.put("LRU", LRUCache::new);
        algorithmCahceSeupplierMap.put("LFU", LFUCache::new);
    }

    @Override
    public Cache getCache(String name) {
        return Optional.ofNullable(cacheMap.get(name))
                .orElseGet(() ->
                        Optional.ofNullable(algorithmCahceSeupplierMap.get(algorithm))
                                .map(cacheSupplier -> cacheSupplier.get(capacity, name))
                                .orElseThrow()
                );
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    private interface CacheSupplier<C, N, T> {
        T get(C capacity, N name);
    }
}
