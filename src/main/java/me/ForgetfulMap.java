package me;

public interface ForgetfulMap<K, V> {
    public void add(K key, V value);

    public V find(K key);
}
