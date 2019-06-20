package org.tranphucbol.predictivetext;

import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.List;

public class BloomFilter implements Dictionary {

    private BitSet bitSet;
    private int size;
    private int k;

    public BloomFilter(int size, int k) {
        this.size = size;
        this.k = k;
        this.bitSet = new BitSet(size);
    }

    private int _hash(String key, int a) {
        long hash = 0;

        char[] cKeys = key.toCharArray();
        int len = cKeys.length;

        for (int i = 0; i < len; i++) {
            hash += Math.pow(a, len - (i+1)) * (0x000000FF & (int)cKeys[i]);
            hash = hash % size;
        }

        return (int)hash;
    }

    public boolean contains(String word) {
        for(int i=0; i<k; i++) {
            if(!bitSet.get(hash(word, i))) {
                return false;
            }
        }
        return true;
    }

    private int hash(String key, int attempt) {
        int hash_a = _hash(key, 7);
        int hash_b = _hash(key, 11);
        return (hash_a + (attempt * (hash_b + 1))) % size;
    }

    public void add(String word) {
        for(int i=0; i<k; i++) {
            bitSet.set(hash(word, i), true);
        }
    }

    public void addFromFile(String folderName, FileUtils reader) {
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {
            try {

                List<String> words = reader.read(file);
                for (String word : words) {
                    this.add(word);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
