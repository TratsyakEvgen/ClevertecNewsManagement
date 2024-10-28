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
    private final ConfigCacheManager config;
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();
    private final Map<String, CacheSupplier<Integer, String, Cache>> algorithmCahceSupplierMap = new HashMap<>();

    {
        algorithmCahceSupplierMap.put("LRU", LRUCache::new);
        algorithmCahceSupplierMap.put("LFU", LFUCache::new);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if (cache == null) {
            String algorithm = config.getAlgorithm();
            cache = Optional.ofNullable(algorithmCahceSupplierMap.get(algorithm))
                    .map(cacheSupplier -> cacheSupplier.get(config.getCapacity(), name))
                    .orElseThrow(() -> new IncorrectAlgorithmException(algorithm + " is not support"));
            cacheMap.put(name, cache);
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }

    private interface CacheSupplier<C, N, T> {
        T get(C capacity, N name);
    }
}
