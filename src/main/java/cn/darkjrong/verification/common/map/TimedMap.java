package cn.darkjrong.verification.common.map;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimedMap<K, V> {
    private final ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<K, Long> expirationTimes = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void put(K key, V value, long duration, TimeUnit timeUnit) {
        map.put(key, value);
        long expirationTime = System.currentTimeMillis() + timeUnit.toMillis(duration);
        expirationTimes.put(key, expirationTime);
        scheduler.schedule(() -> {
            map.remove(key);
            expirationTimes.remove(key);
        }, duration, timeUnit);
    }

    public void put(K key, V value, Duration duration) {
        map.put(key, value);
        long expirationTime = System.currentTimeMillis() + duration.toMillis();
        expirationTimes.put(key, expirationTime);
        scheduler.schedule(() -> {
            map.remove(key);
            expirationTimes.remove(key);
        }, duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public V get(K key) {
        return map.get(key);
    }




}

