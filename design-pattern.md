# DESIGN PATTERN

## Singleton

Singleton thuộc nhóm Creational Design Pattern

Singleton là một degign pattern mà:

1. Đảm bảo rắng một class chỉ có duy nhất một instance.
2. Cung cấp một cách global để truy cập tới instance đó.

<div align="center">
    <img src="images/singleton.png">
</div>

### Tại sao cần sử dụng Singleton

Ví dụ trong code, ta phải tạo ra hàng trăm đối tượng Database ở nhiều chỗ khác nhau trong code, những đối tượng này có cùng một nội dung, và vấn đề là nó rất lớn. Singleton sẽ giúp ta tạo ra tạo ra một đối tượng duy nhất và có thể truy cập bất cứ đâu trong code.

### Code Singleton

```java
public final class Singleton {
    private static Singleton INSTANCE = null;

    private Singleton();

    public static Singleton getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }
}
```

Dùng `Singleton` trong multithreading, thì không thể sử dụng phần code ở phía trên. `Singleton` Threadsafe sẽ như sau:

```java
public static final Singleton {
    private static Singleton INSTANCE = null;

    private Singleton();

    public static Singleton getInstance() {
        if(INSTANCE == null) {
            synchronized(Singleton.class) {
                if(INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }
}
```