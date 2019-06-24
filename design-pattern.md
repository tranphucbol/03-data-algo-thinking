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

## Builder

<div align="center">
    <img src="images/builder-pattern.png">
</div>

Khi hàm khởi tạo của một đối tượng phức tạp. Các thuộc tính cần phải sử dụng các thuật toán để khởi tạo, ta cần tách khởi tạo của từng thuộc tính cho đối tượng để dễ quản lý và code sạch hơn.

Mặc định, Builder pattern gồm có 4 thành phần cơ bản:

- `Product` là đối tượng cần tạo (đối tượng này phức tạp và có nhiều thuộc tính).
- `Builder` là abstract class hoặc interface khai báo phương thức tạo đối tượng.
- `ConcreteBuilder` thừa kế builder và cài đặt chi tiết cách tạo ra đối tượng.
- `Director` là nơi sẽ gọi tới Builder để tạo ra đối tượng.

Thông thường, những trường hợp đơn giản nguwoif ta sẽ gộp luôn Builder và ContreteBuidler thành `static nested class` bên trong `Product`

### Code Builder

```java

public class Car {

    private Wheel wheel;
    private Color color;

    private Car(CarBuilder builder) {
        this.wheel = builder.wheel;
        this.color = builder.color;
    }

    public Wheel getWheel() {
        return this.wheel;
    }

    public Color getColor() {
        return this.color;
    }

    private Car() {}

    public static class CarBuilder {
        private Wheel wheel;
        private Color color;

        public CarBuilder(Wheel wheel, Color color) {
            this.wheel = wheel;
            this.color = color;
        }

        public CarBuilder setWheel(Wheel wheel) {
            this.wheel = wheel;
            return this;
        }

        public CarBuilder setColor(Color color) {
            this.color = color;
            return this;
        }

        public Car build() {
            return new Computer(this);
        }
    }

}

```

## Factory Method

<div align="center">
    <img src="images/factory-method.png">
</div>

`Factory Method` pattern là một creational pattern mà sử dụng các factory method để giải quyết vấn đề tạo đối tượng mà không phỉa chỉ định class chính xác của đối tượng sẽ được tạo. Điều này được thực hiện bằng các tạo các đối tượng bằng cách gọi một factory method. Hoặc được chỉ địh một interface và implemented bởi class con, hoặc implemented trong một class cha và được ghi đè tùy ý bởi các lớp dẫn xuất thay vì gọi một constructor.

Dùng `Factory Method` pattern khi:

- Một class không thể lường trước lớp đối tượng mà nó phải tạo
- Một class muốn các lớp con của nó chỉ định các đối tượng mà nó tạo ra

### Code Factory Method

```java
public interface Shape {
    String getType();
}

public class Rectangle implements Shape {
    public String getType() {
        return "Rectangle";
    }
}

public class Circle implements Shape {
    public String getType() {
        return "Circle";
    }
}

public class ShapeFactory {
    public static final int RECTANGLE = 1;
    public static final int CIRCLE = 2;

    public static final Logger LOGGER = LoggerFactory.getLogger(ShapeFactory.class);

    public Shape createShape(int type) {
        switch (type) {
            case RECTANGLE:
                LOGGER.info("Create Rectangle");
                return new Rectangle();
            case CIRCLE:
                LOGGER.info("Create Circle");
                return new Circle();
            default:
                LOGGER.error("This type does not exist!");
                return null;
        }
    }
}
```

## Strategy

<div align="center">
    <img src="images/stragegy.png">
</div>

`Strategy` pattern là một behavioral pattern. `Strategy` pattern được sử dụng khi ta có nhiều thuật toán cho một task nhất định và client quyết định thực hiện sẽ được sử dụng trong runtime.

Dùng `Strategy` pattern khi:

- Nhiều class liên quan chỉ khác nhau trong behavior của nó. Strategies cung cấp một để cấu hình một class hoặc một trong nhiều behavior.
- Bạn cần nhiều biến thể khác nhau của một thuật toán.
- Một class định nghĩa nhiều behavior và chúng xuất hiện dưới dạng câu điều kiện trong các operation của nó. Thay vì sử dụng nhiều câu điều kiện, chuyển các nhánh có điều kiện liên quan vào một Strategy class của riêng nó.

### Code Strategy

```java
public interface SendMessageStrategy {
    void send(String message);
}

public class SMSStrategy implements SendMessageStrategy {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}

public class MailStrategy implements SendMessageStrategy {
    public void send(String message) {
        System.out.println("Mail: " + message);
    }
}

public class SendMessageContext {
    private SendMessageStrategy strategy;

    public void setSendMessageStrategy(SendMessageStrategy strategy) {
        this.strategy = strategy;
    }

    public void sendMessage(String message) {
        strategy.send(message);
    }
}
```

## Dependency Injection

`Dependency Injection` là một design pattern cho phép xóa bỏ sự phụ thuộc giữa class cấp cao và class cấp thấp, làm cho ứng dụng ít bị kết dính, dễ dàng mở rộng và maintain hơn.

Lấy một ví dụ theo một trang đã kham khảo:

Nguồn: [https://stackjava.com/design-pattern/dependency-injection-di-la-gi.html](https://stackjava.com/design-pattern/dependency-injection-di-la-gi.html)

Mình có 1 ứng dụng gọi tới object của class MySQLDAO(class MySQLDAO chuyên thực hiện truy vấn với cơ sở dữ liệu MySQL của ứng dụng)

Bây giờ bạn muốn truy vấn tới cơ sở dữ liệu postgre. Bạn phải xóa khai báo MySQLDAO trong ứng dụng và thay bằng PostgreDAO, sau đó muốn dùng lại MySQLDAO bạn lại làm ngược lại… rõ ràng code sẽ phải sửa lại và test nhiều lần.

Giải pháp dùng if-else kiểm tra điều kiện sẽ dùng đối tượng DAO nào… nhưng sau đấy có thêm một DAO khác ví dụ như MSSQLDAO chẳng hạn… phức tạp hơn nhiều phải không.

Dependency Inject chính là để giải quyết cho trường hợp như thế này.

Trong ví dụ trên, tạo ra một interface AbstractDAP vào cho các class DAO kia implements AbstractDAO.

### Các phương pháp thực hiện Dependency Injection

1. **Construcotr Injection:** Các dependency sẽ được container truyền vào 1 class thông qua constructor của class đó.
2. **Setter Injection:** Các dependency sẽ được truyền vào 1 class thông qua các hàm Setter.
3. **Interface Injection:** Class cần inject sẽ implement 1 interface. Interface này được chứa 1 hàm tên `Inject`. Container sẽ injection dependency vào 1 class thông qua việc gọi hàm `Inject` của interface đó.

### Ưu điểm Dependency Injection

- Giảm sự kết dính giữa các module
- Code dễ bảo trì, dễ thay thế module
- Rất dễ test và viết Unit Test
- Dễ dàng thấy quan hệ giữa các module (Vì các dependency đều được inject vào contructor)

### Nhược điểm Dependency Injection

- Khái niệm DI hơi khó hiueer với người mới.
- Khó debug vì không biết implements nào của interface được gọi đến
- Các object được khởi tạo đầu làm giảm performance
- Làm tăng độ phức tạo của code.

## Reference

- [Factory Method](https://en.wikipedia.org/wiki/Factory_method_pattern)
- [Builder](https://sourcemaking.com/design_patterns/builder)
- [Stragety](https://www.journaldev.com/1754/strategy-design-pattern-in-java-example-tutorial)
- [Stragety](https://sourcemaking.com/design_patterns/strategy)
- [Dependency Injection](https://www.freecodecamp.org/news/a-quick-intro-to-dependency-injection-what-it-is-and-when-to-use-it-7578c84fa88f/)
