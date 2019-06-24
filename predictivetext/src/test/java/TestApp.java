import org.junit.Test;
import org.tranphucbol.predictivetext.BlogFile;
import org.tranphucbol.predictivetext.BloomFilter;
import org.tranphucbol.predictivetext.Dictionary;
import org.tranphucbol.predictivetext.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestApp {
    

    @Test
    public void run() throws IOException {
        FileUtils reader = new BlogFile();
        List<String> words = reader.read(new File("/home/cpu11413/Documents/blogs/999503.male.25.Internet.Cancer.xml"));

        BloomFilter bloomFilter = new BloomFilter(1000000);
        bloomFilter.addFromFile("/home/cpu11413/Documents/blogs/", reader);

        int count = 0;

        for(String word : words) {
            if (bloomFilter.contains(word)) {
                count++;
            }
        }

        System.out.println(count * 1.0 / words.size());
    }
}
