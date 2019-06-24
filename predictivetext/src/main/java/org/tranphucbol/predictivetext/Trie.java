package org.tranphucbol.predictivetext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trie implements Dictionary, Suggestion {
    private TrieNode root;
    private boolean done = false;

    private static final Logger logger = LoggerFactory.getLogger(Trie.class);

    public Trie() {
        root = new TrieNode();
        root.label = '#';
    }

    public Trie(String folderName, FileUtils reader) {
        root = new TrieNode();
        root.label = '#';
        this.addFromFile(folderName, reader);
    }

    public void add(String word) {
        if(word.equals("phuckity")) {
            System.out.println("helol");
        }
        char[] wc = word.toCharArray();

        TrieNode next = root;
        for (int i = 0; i < wc.length; i++) {
            if (wc[i] - 32 > 94 || wc[i] - 32 < 0) {
                return;
            }
            if (next.links[wc[i] - 32] == null) {
                next.count++;
                next.links[wc[i] - 32] = new TrieNode(wc[i]);
            }
            next = next.links[wc[i] - 32];
        }
        next.flag = true;
    }

    private boolean isLastNode(TrieNode root) {
        for(int i = 0; i < root.links.length; i++) {
            if(root.links[i] != null) {
                return false;
            }
        }
        return true;
    }

    private void  getWord(TrieNode node, String prefix, List<String> results) {
        if (node.flag) {
            results.add(prefix + node.label);
        }

        if(isLastNode(node)) {
            return;
        }

        for (int i = 0; i < node.links.length; i++) {
            if (node.links[i] != null) {
                getWord(node.links[i], prefix + node.label, results);
            }
        }
    }

    public void addFromFile(String folderName, FileUtils reader) {
        File folder = new File(folderName);
        logger.info("reading data from " + folderName);
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
        done = true;
    }

    private void writeTrieNode(TrieNode node, DataOutputStream out) throws IOException {
        out.writeByte(node.label);
        out.writeInt(node.count);
        out.writeBoolean(node.flag);
        for (int i = 0; i < node.links.length; i++) {
            if (node.links[i] != null) {
                writeTrieNode(node.links[i], out);
            }
        }
    }

    private TrieNode readTrieNode(DataInputStream in) throws IOException {
        TrieNode node = new TrieNode();
        node.label = (char) in.readByte();
        int len = in.readInt();
        node.flag = in.readBoolean();
        for (int i = 0; i < len; i++) {
            TrieNode child = readTrieNode(in);
            node.links[child.label - 32] = child;
        }
        return node;
    }

    private void readFile() {
        try {
            logger.info("reading file");
            DataInputStream in = new DataInputStream(new FileInputStream("Trie"));
            root = readTrieNode(in);
            in.close();
            done = true;
            logger.info("done");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTrie() {
        try {
            logger.info("writing file");
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream("Trie"));
            writeTrieNode(root, out);
            done = true;
            logger.info("done");
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String word) {
        char[] wc = word.toCharArray();

        TrieNode next = root;
        for (int i = 0; i < wc.length; i++) {
            if (next.links[wc[i] - 32] == null) {
                return false;
            }
            next = next.links[wc[i] - 32];
        }
        return (next != null && next.flag);
    }

    public List<String> startWith(String prefix) {
        List<String> results = new ArrayList<String>();

        if (prefix == null || prefix.equals("")) {
            return Collections.EMPTY_LIST;
        }

        TrieNode next = root;

        char[] wc = prefix.toCharArray();

        for(int i=0; i<wc.length; i++) {
            if(next.links[wc[i] - 32] == null) {
                return Collections.EMPTY_LIST;
            }
            next = next.links[wc[i] - 32];
        }

        getWord(next, prefix.substring(0, prefix.length() - 1), results);
        return results;
    }

    public static Trie read() {
        Trie trie = new Trie();
        trie.readFile();
        return trie;
    }
}
