package ru.clevertec.cache.cache;

import org.springframework.cache.support.SimpleValueWrapper;
import ru.clevertec.cache.manager.AbstractAlgorithmCache;

import java.util.LinkedHashMap;
import java.util.Map;


public class LRUCache extends AbstractAlgorithmCache {
    private final Map<Object, Object> cache;

    public LRUCache(int capacity, String name) {
        super(capacity, name);
        this.cache = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
                return size() > capacity;
            }
        };
    }

    @Override
    public synchronized ValueWrapper get(Object key) {
        Object object = cache.get(key);
        return object != null ? new SimpleValueWrapper(object) : null;
    }

    @Override
    public synchronized void put(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public synchronized void evict(Object key) {
        cache.remove(key);
    }

    @Override
    public synchronized void clear() {
        cache.clear();
    }

}

