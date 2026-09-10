package br.com.dbrreto.cache.impl;

import br.com.dbrreto.cache.ExpiringCache;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiringCacheImpl<K, V> implements ExpiringCache<K, V>, AutoCloseable {

    private final ScheduledExecutorService cleaner;
    private final Map<K, CacheEntry<V>> entries;
    private Clock clock;

    public ExpiringCacheImpl() {
        this(Clock.systemDefaultZone(), Executors.newSingleThreadScheduledExecutor());
    }

    public ExpiringCacheImpl(Clock clock, ScheduledExecutorService cleaner) {
        this.entries = new ConcurrentHashMap<>();
        this.cleaner = cleaner;
        this.cleaner.scheduleAtFixedRate(this::evictExpired, 1, 1, TimeUnit.SECONDS);
        this.clock = clock;
    }

    private void evictExpired() {
        entries.forEach((key, entry) ->
            entries.computeIfPresent(key, (k, v) -> isExpired(v) ? null : v)
        );
    }

    @Override
    public void close() {
        cleaner.shutdown();
    }

    @Override
    public void put(K key, V value, long ttlMillis) {
        entries.put(key, new CacheEntry<>(value, ttlMillis, clock.instant()));
    }

    @Override
    public V get(K key) {
        var entry = entries.computeIfPresent(key, (k, v) -> isExpired(v) ? null : v);
        return entry == null ? null : entry.value();
    }

    @Override
    public void remove(K key) {
        entries.remove(key);
    }

    private boolean isExpired(CacheEntry<V> entry) {
        if (entry != null) {
            var createdAt = entry.createdAt();
            var ttl = entry.ttlMillis();
            return createdAt.plus(ttl, ChronoUnit.MILLIS).isBefore(clock.instant());
        }
        return false;
    }

    record CacheEntry<V> (V value, long ttlMillis, Instant createdAt) {}
}

