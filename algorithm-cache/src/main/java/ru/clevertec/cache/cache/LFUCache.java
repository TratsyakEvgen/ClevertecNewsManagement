package ru.clevertec.cache.cache;

import org.springframework.cache.support.SimpleValueWrapper;
import ru.clevertec.cache.manager.AbstractAlgorithmCache;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LFUCache extends AbstractAlgorithmCache {
    private final Map<Object, Object> cache;
    private final Map<Object, Integer> frequencyKeyMap;

    public LFUCache(int capacity, String name) {
        super(capacity, name);
        this.cache = new HashMap<>();
        this.frequencyKeyMap = new HashMap<>();
    }

    @Override
    public synchronized ValueWrapper get(Object key) {
        if (cache.containsKey(key)) {
            incrementFrequency(key);
            return new SimpleValueWrapper(cache.get(key));
        }
        return null;
    }

    @Override
    public synchronized void put(Object key, Object value) {
        cache.put(key, value);
        incrementFrequency(key);

        if (cache.size() >= capacity) {
            removeMinUsedValue();
        }
    }

    @Override
    public synchronized void evict(Object key) {
        cache.remove(key);
        frequencyKeyMap.remove(key);
    }

    @Override
    public synchronized void clear() {
        cache.clear();
        frequencyKeyMap.clear();
    }

    private synchronized void removeMinUsedValue() {
        frequencyKeyMap.entrySet()
                .stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .ifPresent(key -> {
                    cache.remove(key);
                    frequencyKeyMap.remove(key);
                });
    }

    private synchronized void incrementFrequency(Object key) {
        int frequency = Optional.ofNullable(frequencyKeyMap.get(key))
                .orElse(0);
        frequencyKeyMap.put(key, frequency + 1);
    }
}
