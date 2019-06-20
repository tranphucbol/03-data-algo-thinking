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

    private Trie() {
        root = new TrieNode();
        root.label = '#';
    }

    public Trie(String folderName, FileUtils reader) {
        root = new TrieNode();
        root.label = '#';
        this.addFromFile(folderName, reader);
    }

    public void add(String word) {
        char[] wc = word.toCharArray();

        TrieNode next = root;
        next.flag = true;
        for (int i = 0; i < wc.length; i++) {
            if (wc[i] - 32 > 94 || wc[i] - 32 < 0) {
                return;
            }
            if (next.links[wc[i] - 32] == null) {
                next.count++;
                next.links[wc[i] - 32] = new TrieNode(wc[i]);
            }
            next = next.links[wc[i] - 32];
            next.flag = true;
        }
        next.flag = false;
    }

    private void getWord(TrieNode node, String s, List<String> results) {
        if (!node.flag) {
            results.add(s + node.label);
        }
        for (int i = 0; i < node.links.length; i++) {
            if (node.links[i] != null) {
                getWord(node.links[i], s + node.label, results);
            }
        }
    }

//    public void addFromFile(String folderName) {
//        File folder = new File(folderName);
//        processBar("Reading data ");
//        for (File file : folder.listFiles()) {
//            try {
//                FileReader reader = new FileReader(file);
//                BufferedReader br = new BufferedReader(reader);
//
//                // read line by line
//                String line;
//                while ((line = br.readLine()) != null) {
//                    line = line.replaceAll("<Blog>|</Blog>|<post>|</post>|<date>|</date>|&", "").trim();
//                    String[] words = line.split(" *[.,\\-:;?(){}\\[\\]\"\n\t\r! ]+ *| +");
//                    for (String w : words) {
//                        this.add(w);
//                    }
//                }
//            } catch (IOException e) {
//                System.err.format("IOException: %s%n", e);
//            }
//        }
//        done = true;
//    }

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

//    private void processBar(final String message) {
//        done = false;
//        new Thread(new Runnable() {
//            public void run() {
//                int count = 0;
//                int maxCount = 3;
//                while (!done) {
//                    try {
//                        Thread.sleep(500);
//                        StringBuffer buffer = new StringBuffer(message);
//
//                        if (count == maxCount)
//                            count = 0;
//                        else count++;
//
//                        for (int i = 0; i < count; i++) {
//                            buffer.append('.');
//                        }
//
//                        System.out.print(buffer.toString() + "\r");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                System.out.println("\nDone!");
//            }
//        }).start();
//    }

    public boolean contains(String word) {
        char[] wc = word.toCharArray();

        TrieNode next = root;
        for (int i = 0; i < wc.length; i++) {
            if (next.links[wc[i] - 32] == null) {
                return false;
            }
            next = next.links[wc[i] - 32];
        }
        return true;
    }

    public List<String> startWith(String word) {
        List<String> results = new ArrayList<String>();

        if (word == null || word.equals("")) {
            return Collections.EMPTY_LIST;
        }

        char[] wc = word.toCharArray();

        TrieNode next = root;

        for (int i = 0; i < wc.length; i++) {
            if (next.links[wc[i] - 32] == null) {
                return results;
            }
            next = next.links[wc[i] - 32];
        }

        getWord(next, word.substring(0, word.length() - 1), results);
        return results;
    }

    public static Trie read() {
        Trie trie = new Trie();
        trie.readFile();
        return trie;
    }
}
