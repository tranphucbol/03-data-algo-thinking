package org.tranphucbol.predictivetext;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        long start, end, total;
        Trie trie = new Trie("/home/cpu11413/Documents/blogs", new BlogFile());
//        File folder = new File("/home/cpu11413/Documents/blogs");
        trie.writeTrie();

//        Trie trie = new Trie.Builder()
//                .setRead(true)
//                .setWrite(true)
//                .build();

        new DictionaryUI();
    }


}
