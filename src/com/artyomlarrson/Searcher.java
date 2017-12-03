package com.artyomlarrson;

import java.io.File;
import java.util.List;

/**
 * Interface which represents simple search mechanism
 * @since Java 8
 * @author Artyom
 * @version 0.1
 */
public interface Searcher {
    public List<File> search(String criteria);
}
