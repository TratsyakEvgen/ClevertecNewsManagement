package ru.clevertec.cache.cache;

import lombok.Getter;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация кэша с алгоритмом LFU - при переполнении удаляется элемент с наименьшей частотой обращения.
 * В данной реализации операция put так же является обращением к элементу.
 */
@Getter
public class LFUCache extends AbstractAlgorithmCache {
    private final Map<Object, Object> cache;
    private final Map<Object, Integer> frequencyKeyMap;

    public LFUCache(int capacity, String name) {
        super(capacity, name);
        this.cache = new HashMap<>();
        this.frequencyKeyMap = new HashMap<>();
    }

    /**
     * Возвращает обертку кэшируемого значения или null(если объект не найден),
     * инкриминирует частоту обращения к кэшируемому объекту
     *
     * @param key ключ по которому будет возвращено значение
     * @return обертка кэшируемо значения
     */
    @Override
    public synchronized ValueWrapper get(Object key) {
        if (cache.containsKey(key)) {
            incrementFrequency(key);
            return new SimpleValueWrapper(cache.get(key));
        }
        return null;
    }

    /**
     * Помещает в кэш объект, если данный объект существует, то инкриминируется частота обращения к кэшируемому объекту.
     * В случае переполнения кэша помещаемый объект вытесняет элемент с наименьшей частотой обращения.
     *
     * @param key   ключ, по которому будет осуществляться сохранение значения
     * @param value кэшируемое значение
     */
    @Override
    public synchronized void put(Object key, Object value) {
        if (!cache.containsKey(key) && cache.size() == capacity) {
            removeMinUsedValue();
        }
        cache.put(key, value);
        incrementFrequency(key);
    }

    /**
     * Удаляет значение из кэша и информацию о частоте обращения
     *
     * @param key ключ по которому значение из кэша удаляется
     */
    @Override
    public synchronized void evict(Object key) {
        cache.remove(key);
        frequencyKeyMap.remove(key);
    }

    /**
     * Очистка всего кэша
     */
    @Override
    public synchronized void clear() {
        cache.clear();
        frequencyKeyMap.clear();
    }

    /**
     * Удаляет из кэша наименее редкое, по частоте обращений, значение
     */
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

    /**
     * Инкриминирует частоту обращения к кэшируемому объекту по его ключу
     *
     * @param key ключ кэшируемого значения
     */
    private synchronized void incrementFrequency(Object key) {
        int frequency = Optional.ofNullable(frequencyKeyMap.get(key))
                .orElse(0);
        frequencyKeyMap.put(key, frequency + 1);
    }
}
