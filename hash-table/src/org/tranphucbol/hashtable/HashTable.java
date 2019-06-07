package org.tranphucbol.hashtable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class HashTable<K, V> {
    private int size;
    private HashTableItem<K, V>[] items;
    private int collision;
    private int count;

    private static final HashTableItem HT_DELETE_ITEM = new HashTableItem(null, null);

    public static final int LINEAR_HASHING = 1;
    public static final int DOUBLE_HASHING = 2;
    public static final int QUADRATIC_HASHING = 3;

    public static final int HT_INITIAL_BASE_SIZE = 11;

    public HashTable() {
        this._newSized(HT_INITIAL_BASE_SIZE);
        this.collision = DOUBLE_HASHING;
    }

    public HashTable(int size) {
        this._newSized(size);
        this.collision = DOUBLE_HASHING;
    }

    public HashTable(int size, int collision) {
        this._newSized(size);
        this.collision = collision;
    }

    private int _hash(K key, int a) {
        long hash = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(key);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

         byte[] cKeys = bos.toByteArray();
        int len = cKeys.length;

            for (int i = 0; i < len; i++) {
            hash += Math.pow(a, len - (i+1)) * (0x000000FF & (int)cKeys[i]);
            hash = hash % size;
        }

        return (int)hash;
    }

    private int hash(K key, int attempt) {
        int index = -1;
        switch (collision) {
            case DOUBLE_HASHING:
                index = doubleHashing(key, attempt);
                break;
            case QUADRATIC_HASHING:
                index = quadraticHashing(key, attempt);
                break;
            default:
                index = linearHashing(key, attempt);
                break;
        }
        return index;
    }

    public void insert(K key, V value) {
        int load = this.count * 100 / this.size;
        if(load > 70) {
            _resizeUp();
        }
        int index = hash(key, 0);
        HashTableItem item = items[index];
        int i = 1;
        while(item != null && item != HT_DELETE_ITEM && i < size) {
            index = hash(key, i);
            item = items[index];
            i++;
        }
        items[index] = new HashTableItem(key, value);
        count++;
    }

    public V search(K key) {
        int index = hash(key, 0);
        HashTableItem item = items[index];
        int i = 1;
        while(item != null && i < size) {
            if(key.equals(item.key)) {
                return (V)item.value;
            }
            index = hash(key, i);
            item = items[index];
            i++;
        }
        return null;
    }

    public void delete (K key) {
        int load = this.count * 100 / this.size;
        if(load < 10) {
            _resizeDown();
        }
        int index = hash(key, 0);
        HashTableItem item = items[index];
        int i = 1;
        while(item != null && item != HT_DELETE_ITEM && i < size) {
            if(item.key.equals(key)) {
                items[index] = HT_DELETE_ITEM;
                count--;
                break;
            }
            index = hash(key, i);
            item = items[index];
            i++;
        }
    }

    private int doubleHashing(K key, int attempt) {
        int hash_a = _hash(key, 7);
        int hash_b = _hash(key, 11);
        return (hash_a + (attempt * (hash_b + 1))) % size;
    }

    private int linearHashing(K key, int attempt) {
        return (_hash(key, 123) + attempt) % size;
    }

    private int quadraticHashing(K key, int attempt) {
        return (_hash(key, 123) + 13 * attempt + 17 * attempt * attempt) % size;
    }

    private void _newSized(int size) {
        this.size = nextPrime(size);
        this.count = 0;
        this.items = new HashTableItem[this.size];
    }

    /*
    1 prime
    0 not prime
    -1 undefine
     */
    private int isPrime(int x) {
        if(x < 2) return -1;
        if(x < 4) return 1;
        if(x % 2 == 0) return 0;
        for (int i = 3; i <= Math.floor(Math.sqrt((double)x)); i += 2) {
            if(x % i == 0) {
                return 0;
            }
        }
        return 1;
    }

    private int nextPrime(int x) {
        while(isPrime(x) != 1) {
            x++;
        }
        return x;
    }

    public void resize(int size) {
        if(size < HT_INITIAL_BASE_SIZE || size == this.size) return;

        HashTable<K, V> newHt = new HashTable<>(size, this.collision);
        for(int i = 0; i < this.size; i++) {
            if(this.items[i] != null && this.items[i] != HT_DELETE_ITEM) {
                newHt.insert(this.items[i].key, this.items[i].value);
            }
        }

        this.count = newHt.count;
        this.size = newHt.size;
        this.items = newHt.items;
    }

    private void _resizeUp() {
        resize(this.size * 2);
    }

    private void _resizeDown() {
        resize(this.size / 2);
    }

    public int getSize() {
        return size;
    }

    public int getCollision() {
        return collision;
    }

    public int getCount() {
        return count;
    }
}
