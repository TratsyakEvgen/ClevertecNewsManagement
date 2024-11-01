package ru.clevertec.cache.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

@RequiredArgsConstructor
public abstract class AbstractAlgorithmCache implements Cache {
    protected final int capacity;
    private final String name;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        throw new UnsupportedOperationException("getNativeCache method is unsupported");
    }

    @Override
    public abstract ValueWrapper get(Object key);

    @Override
    public <T> T get(Object key, Class<T> type) {
        throw new UnsupportedOperationException("get method by key and class is unsupported");
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        throw new UnsupportedOperationException("get method by key and valueLoader is unsupported");
    }

    @Override
    public abstract void put(Object key, Object value);

    @Override
    public abstract void evict(Object key);

    @Override
    public abstract void clear();
}
