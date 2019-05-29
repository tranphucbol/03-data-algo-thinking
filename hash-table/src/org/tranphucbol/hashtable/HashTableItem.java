package org.tranphucbol.hashtable;

public class HashTableItem<K, V> {
    public K key;
    public V value;

    public HashTableItem(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
