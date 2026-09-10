package br.com.dbrreto.cache;

public interface ExpiringCache<K, V> {

    void put(K key, V value, long ttlMilis);
    V get(K key);
    void remove(K key);
}
