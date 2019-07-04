package org.tranphucbol.predictivetext;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        new DictionaryUI();
        BloomFilter bloomFilter = new BloomFilter(5000000);
        bloomFilter.readFile();
    }


}