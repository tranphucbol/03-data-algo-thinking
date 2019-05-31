package org.tranphucbol.predictivetext;

public class TrieNode {
    public boolean flag;
    public TrieNode[] links;
    public char label;
    public int count;

    public TrieNode() {
        this.flag = false;
        this.links = new TrieNode[95];
        this.count = 0;
    }

    public TrieNode(char label) {
        this.flag = false;
        this.links = new TrieNode[95];
        this.label = label;
        this.count = 0;
    }
}
