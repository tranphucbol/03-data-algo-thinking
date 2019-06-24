# Hash Table

## Structrue

Cấu trúc của item trong hash table gôm một cặp `key-value`

```java
public class HashTableItem<K, V> {
    public K key;
    public V value;

    public HashTableItem(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
```

Hash table chứa một array `HashTableItem`, kiểu tránh đụng độ `collision`, số lượng `count` hiện tại và kích thước của hash table `size`.

```java
public class HashTable<K, V> {
    private int size;
    private HashTableItem<K, V>[] items;
    private int collision;
    private int count;
}
```

## Constructor

Các hàm `constructor` bao gồm:

```java
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
```

Hàm `_newSized` sẽ tạo ra mảng `HashTableItem` với số size là số nguyên tố tiếp theo gần nhất của số `size` truyền vào.

## Insert và Delete

### Insert

```java
public void insert(K key, V value);
```

Insert sẽ up size cho hash table nếu như `load factor` > 70%, nếu như key được truyền vào đã tồn tại, thì giá trị `value` sẽ được gán bằng `value` truyền vào.

### Delete

```java
public void delete(K key);
```

Delete sẽ down size cho hash table nếu như `load factor` < 10%.

## Xử lý collision

Hash table hỗ trợ 3 cách tránh đụng độ, tất cả đều là `Probing`:

- Linear Hashing
- Double Hashing (Cách này là mặc định)
- Quaratic Hashing

```java
public static final int LINEAR_HASHING = 1;
public static final int DOUBLE_HASHING = 2;
public static final int QUADRATIC_HASHING = 3;
```
