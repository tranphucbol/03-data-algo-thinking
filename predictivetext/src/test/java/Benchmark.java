import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.tranphucbol.predictivetext.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Benchmark {

    private FileUtils reader = new BlogFile();
    private BloomFilter bloomFilter;
    private Trie trie;

    @DataProvider(name = "testCase")
    public static Object[][] testCase() throws IOException {
        List<String> words = new BlogFile().read(new File("/home/cpu11413/Documents/blogs/998237.female.16.indUnk.Virgo.xml"));
        Object[][] testCase = new Object[words.size()][1];
        int i = 0;
        for(String word : words) {
            testCase[i][0] = word;
            i++;
        }
        return testCase;
    }

    @BeforeClass
    public void init() throws IOException {
        bloomFilter = new BloomFilter(5000000);
        bloomFilter.readFile();

        trie = Trie.read();
    }

    @Test(dataProvider = "testCase")
    public void searchBloomFilter(String word) {
        bloomFilter.contains(word);
    }

    @Test(dataProvider = "testCase")
    public void searchTrie(String word) {
        trie.contains(word);
    }

    @Test(dataProvider = "testCase")
    public void predictiveTrie(String word) {
        trie.startWith(word);
    }
}
