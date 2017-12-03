package com.artyomlarrson;

import java.io.File;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        Searcher s = new RecursiveSearcher(RecursiveSearcher.MODE_ANY_BY_PART,
                false, false, true);
        List<File> files = s.search("Steam");

        System.out.println(!files.isEmpty() ? "Found files:" : "No files found.");
        for (File f : files) {
            System.out.println(f.getAbsolutePath());
        }
    }

}
