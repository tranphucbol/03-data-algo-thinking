package org.tranphucbol.hashtable;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        HashTable<String, Integer> ht = new HashTable<>();
        ht.insert("dog", 1);
        ht.insert("dog", 2);
        System.out.println(ht.search("dog"));
    }
}
