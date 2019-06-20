package org.tranphucbol.predictivetext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlogFile implements FileUtils {
    private static final Logger logger = LoggerFactory.getLogger(BlogFile.class);
    public List<String> read(File file) throws IOException {
        FileReader reader = new FileReader(file);
        BufferedReader br = new BufferedReader(reader);

        // read line by line
        String line;
        List<String> words = new ArrayList<String>();
        while ((line = br.readLine()) != null) {
            line = line.replaceAll("<Blog>|</Blog>|<post>|</post>|<date>|</date>|&", "").trim();
            words.addAll(Arrays.asList(line.split(" *[.,\\-:;?(){}\\[\\]\"\n\t\r! ]+ *| +")));
        }

        br.close();
        reader.close();
        logger.info(file.getName());
        return words;
    }
}
