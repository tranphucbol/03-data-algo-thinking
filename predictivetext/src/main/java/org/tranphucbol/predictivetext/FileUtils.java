package org.tranphucbol.predictivetext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileUtils {
    List<String> read(File file) throws IOException;
}
