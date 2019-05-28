# Cấu trúc dữ liệu

- [Cấu trúc dữ liệu](#c%E1%BA%A5u-tr%C3%BAc-d%E1%BB%AF-li%E1%BB%87u)
  - [Bloom Filters](#bloom-filters)
    - [Cách hoạt động Bloom Filters](#c%C3%A1ch-ho%E1%BA%A1t-%C4%91%E1%BB%99ng-bloom-filters)
    - [Hash function trong Bloom Filter](#hash-function-trong-bloom-filter)
    - [Đánh giá Bloom Filter](#%C4%91%C3%A1nh-gi%C3%A1-bloom-filter)
  - [Cuckoo Filters](#cuckoo-filters)
    - [Cách hoạt động Cuckoo Filters](#c%C3%A1ch-ho%E1%BA%A1t-%C4%91%E1%BB%99ng-cuckoo-filters)
  - [Tham khảo](#tham-kh%E1%BA%A3o)

## Bloom Filters

### Cách hoạt động Bloom Filters

`Bloom filter` là một cấu trúc dữ liệu xác xuất để kiểm tra xem 1 phần tử có thuộc 1 tập dữ liệu hay không một cách nhanh chóng và tiết kiệm bộ nhớ.

Bản chất của bloom filter là một vector các bit. Một bloom filter rỗng là một vector các bit có giá trị là 0. Nó cần k hàm hash để map một cách ngẫu nhiên và đồng đều các phần tử vào mảng bit. Sống lượng hàm hash và độ dài của vector bit sẽ ảnh hưởng đến độ chính xác của kết quả.

Bloom filter có 2 tác vụ cơ bản: **Thêm** và **Kiểm tra**

- **Thêm:** khi thêm một phần tử mới trong S vào, dùng k hàm Hash với đầu vào là phần tử mới đó ta sẽ được k vị trí các bit được bật lên trong chuỗi m bit.
- **Kiểm tra:** Khi kiểm tra một phần tử thì cũng sử dụng k hàm Hash với đầu vào là phần tử có tính ra k vị trí trong mảng. Sau đó kiểm tra xem tất cả các bit đó có giá trị 1 hay không. Chỉ chần phát hiện một vị trí có giá trị là 0 phầ n tử đó chắc chắn không nằm trong danh sách. Còn tất cả các vị trí đều ra giá trị 1 thì **có thể** phần tử đó nằm trong danh sách

<div align="center">
    <img src="images/bloom_2.png">
</div>

### Hash function trong Bloom Filter

**Hash function** sử dụng trong bloom filter nên là những hàm hash có tính độ lập và kết quả là một tập hợp được phân bố một cách đồng đều. Giải pháp cho vấn đề này là **Double Hashing**

***hash<sub>i</sub>(x,m) = (hash<sub>a</sub>(x) + i × hash<sub>b</sub>(x)) mod m***

### Đánh giá Bloom Filter

Việc **kiểm tra**, ta có thể thấy rằng có thể có false positive (không thuộc tập dữ liệu, nhưng hàm hash cho ra k vị trí bit là 1), nhưng không thể có false negative (Do tác vụ thêm một phần tử thì phần tử đó phải có k vị trí là 1, nhưng chỉ cần có một vị trí là 0 là kết luận là negative).

Việc false positive sẽ càng tăng nếu kích thước *m* (độ dài bitarray) càng nhỏ và độ lớn của tập dữ liệu càng tăng.

## Cuckoo Filters

### Cách hoạt động Cuckoo Filters

`Cuckoo Filters` hoạt động bằng cách hasing một mục nhập với một hàm hash và  chền f-bit fingerprint cảu mục nhập vào một vị trí mở trong một trong hai bucket thay thế. Khi cả hai buckets đầy, filter đệ  quy các mục hiện có vào các thùng thay thế của chúng cho đến khi space được tìm thấy hoặc các nỗ lực đã hết. Tra cứu lặp lại hash function và kiểm tra cả hai bucket cho fingerprint. Khi không tìm thấy fingerprint phù hợp, mục nhập chắc chắn không có trong bộ lọc. Khi tìm thấy fingerprint phù hợp trong một trong hai bucket, mục nhập có thể nằm trong bộ lọc.

Fingerprint là một chuỗi bit có kích thước cố định sinh ra từ mỗi phần tử đầu vào bằng cách băm chính phần tử nhập vào.

Bộ lọc Cuckoo sử dụng các tính chât sau của phép `xor (⊕)` để giải quyết việc tifkm kiếm ngược lại vị trí thay thế cho phần tử bị đẩy ra khi xảy ra đụng độ

- (A ⊕ B) ⊕ C = A ⊕ (B ⊕ C) (1)
- A ⊕ A = 0 (2)
- A ⊕ 0  = A (3)

Với mỗi phần tử x đầu vào bảng băm của Cuckoo filter, vị trí hai bucket tiềm năng được xác đinh bằng kỹ thuật partial-ky cuckoo hashing:

- h<sub>1</sub>(x) = hash(x) (4)
- h<sub>2</sub>(x) = h<sub>1</sub> ⊕ hash(f) với f = fingerprint(x) (5)

Sử dụng các tính chất của `xor (⊕)`, ta nhận thấy h<sub>1</sub> có thể được tính ngược từ h<sub>2</sub> và fingerprint f với cùng công thức (5)

- h<sub>1</sub>(x) = h<sub>2</sub>(x) ⊕ hash(f)

Vì h<sub>1</sub>(x) và h<sub>2</sub>(x) có thể suy ra được lẫn nhau bằng việc sử dụng chung một công thức (5), ta có được một công thức chung để tính vị trí bucket *j* tiềm năng còn lại từ vị trí bucket *i* có fingerprint f chưa trong bucket đó:

*j* = *i* ⊕ hash(f)

False positive xảy ra khi một mục nhập khác thêm fingerprint vào một trong hai bucket được kiểm tra.

## Tham khảo

- [Bloom filter (1)](https://dzenanhamzic.com/2017/01/05/bloom-filter-example-in-python/)
- [Bloom filter (2)](https://vietnamlab.vn/2016/09/28/gioi-thieu-ve-bloom-filter/)
- [Cuckoo filter (1)](https://bdupras.github.io/filter-tutorial/)
- [Cukcoo filter (2)](https://hoanglehaithanh.com/bo-loc-cuckoo/#more-4382)