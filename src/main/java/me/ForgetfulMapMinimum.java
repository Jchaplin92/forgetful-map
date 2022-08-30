package me;

public interface ForgetfulMapMinimum<K, C> {
    void add(K key, C content);
    C find(K key);
}
