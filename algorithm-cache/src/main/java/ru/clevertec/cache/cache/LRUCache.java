package ru.clevertec.cache.cache;

import org.springframework.cache.support.SimpleValueWrapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Реализация кэша с алгоритмом LRU
 */
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

    /**
     * Возвращает обертку кэшируемого значения или null(если объект не найден),
     *
     * @param key ключ по которому будет возвращено значение
     * @return обертка кэшируемо значения
     */
    @Override
    public synchronized ValueWrapper get(Object key) {
        Object object = cache.get(key);
        return object != null ? new SimpleValueWrapper(object) : null;
    }

    /**
     * Помещает в кэш значение по его id
     *
     * @param key   ключ, по которому будет осуществляться сохранение значения
     * @param value кэшируемое значение
     */
    @Override
    public synchronized void put(Object key, Object value) {
        cache.put(key, value);
    }

    /**
     * Удаляет значение из кэша по его ключу
     *
     * @param key ключ по которому значение из кэша удаляется
     */
    @Override
    public synchronized void evict(Object key) {
        cache.remove(key);
    }

    /**
     * Очистка всего кэша
     */
    @Override
    public synchronized void clear() {
        cache.clear();
    }

}

