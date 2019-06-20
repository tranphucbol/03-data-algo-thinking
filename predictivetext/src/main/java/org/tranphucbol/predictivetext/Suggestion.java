package org.tranphucbol.predictivetext;

import java.util.List;

public interface Suggestion {
    List<String> startWith(String word);
}
