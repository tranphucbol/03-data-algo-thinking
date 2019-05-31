package org.tranphucbol.predictivetext;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Trie trie = new Trie();
//        File folder = new File("data");
//        trie.addFromFile(folder);
//
//        trie.writeTrie();
        trie.readFile();

        Scanner scanner = new Scanner(System.in);

        String keyword;
        while(!(keyword = scanner.nextLine()).equals("exit")) {
            List<String> results = trie.prefix(keyword);
            for(String r : results)
                System.out.println(r);
        }

    }


}
