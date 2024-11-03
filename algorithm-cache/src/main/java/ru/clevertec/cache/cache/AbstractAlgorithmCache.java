package ru.clevertec.cache.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * Абстрактный кэш
 */
@RequiredArgsConstructor
public abstract class AbstractAlgorithmCache implements Cache {
    protected final int capacity;
    private final String name;


    /**
     * @return имя кэша
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @throws UnsupportedOperationException - неподдерживаемая операция
     */
    @Override
    public Object getNativeCache() {
        throw new UnsupportedOperationException("getNativeCache method is unsupported");
    }

    /**
     * @param key ключ по которому будет возвращено значение
     * @return обертка кэшируемо значения
     */
    @Override
    public abstract ValueWrapper get(Object key);

    /**
     * @throws UnsupportedOperationException - неподдерживаемая операция
     */
    @Override
    public <T> T get(Object key, Class<T> type) {
        throw new UnsupportedOperationException("get method by key and class is unsupported");
    }

    /**
     * @throws UnsupportedOperationException - неподдерживаемая операция
     */
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        throw new UnsupportedOperationException("get method by key and valueLoader is unsupported");
    }

    /**
     * Помещает в кэш значение по его id
     *
     * @param key   ключ, по которому будет осуществляться сохранение значения
     * @param value кэшируемое значение
     */
    @Override
    public abstract void put(Object key, Object value);

    /**
     * Удаляет значение из кэша по его ключу
     *
     * @param key ключ по которому значение из кэша удаляется
     */
    @Override
    public abstract void evict(Object key);

    /**
     * Очистка всего кэша
     */
    @Override
    public abstract void clear();
}
