# Predictive Text

## Class Diagram

<div align="center">
    <img src="../images/predictive-class-diagram.jpg" alt="..." />
</div>

## Ứng dụng Design Pattern

### Dependency Inject

Ứng dụng cho inteface `FileUtils` giảm sự phụ thuộc của hàm `addFromFile`, khi cần thay đổi hàm `reader` không đọc dữ liệu từ file Blog. Ta có thể implement một class khác để đọc dữ liệu tương ứng.

## Kết quả Benchmark

### Search với Bloom Filter

<div align="center">
    <img src="../images/searchBloomFilter.png" alt="..." />
</div>

### Search với Trie

<div align="center">
    <img src="../images/searchTrie.png" alt="..." />
</div>

### Prefix với Trie

<div align="center">
    <img src="../images/predictive.png" alt="...">
</div>