package com.artyomlarrson;

import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 * Search realisation through recursive search() function call
 * @author Artyom
 * @version 0.1
 */
public class RecursiveSearcher implements Searcher {
    public static final int MODE_FILE_BY_NAME = 0xAAA;
    public static final int MODE_FILE_BY_PART = 0xAAB;
    public static final int MODE_FOLDER_BY_NAME = 0xAAC;
    public static final int MODE_FOLDER_BY_PART = 0xAAD;
    public static final int MODE_ANY_BY_NAME = 0xAAE;
    public static final int MODE_ANY_BY_PART = 0xAAF;

    public boolean FLAG_WIN32 = true; // TODO: detect platform in a runtime

    private boolean debug = true;
    private int selectedMode = 0;
    private boolean recursive = true;
    private boolean caseSensitive = false;

    public RecursiveSearcher(int mode) {
        selectedMode = mode;
    }

    public RecursiveSearcher(int mode, boolean caseSensitive,  boolean recursive, boolean debugMode) {
        this(mode);
        this.caseSensitive = caseSensitive;
        this.recursive = recursive;
        this.debug = debugMode;
    }

    private void log(String text) {
        if (debug) System.out.println(text);
    }

    /** Returns list of files with specified search mode and criteria
     * @param criteria Search criteria
     * @return List of files which equals specified criteria by selected search mode
     */
    @Override
    public List<File> search(String criteria) {
        List<File> foundFiles = new LinkedList<>();
        List<File> drives;


        drives = FLAG_WIN32 ? listDrivesWin32() : listDrivesLinux(); // Windows or Linux(TODO: remake)
        for (File drive : drives) {
            foundFiles.addAll(searchInDirectory(drive, criteria));
        }

        return foundFiles;
    }

    /** Returns list of files with specified name in directory(and subdirectories)
     * @param directory Directory where to search for
     * @param name Name of file to found
     * @return List of files with specified name
     */
    private List<File> searchInDirectory(File directory, String name) {
        List<File> foundFiles = new LinkedList<>();
        File[] dirFiles = directory.listFiles();

        name = caseSensitive ? name : name.toLowerCase();

        if (dirFiles == null)
            return foundFiles;

        log("Searching in " + directory.getAbsolutePath() + "\\");
        for (File f : dirFiles) {
            String fileName = caseSensitive ? f.getName() : f.getName().toLowerCase();
            boolean added = false;

            switch (selectedMode) {
                case MODE_FILE_BY_NAME:
                    if (f.isFile() && fileName.equals(name))
                        added = foundFiles.add(f);
                    break;
                case MODE_FILE_BY_PART:
                    if (f.isFile() && fileName.contains(name))
                        added = foundFiles.add(f);
                    break;
                case MODE_FOLDER_BY_NAME:
                    if (f.isDirectory() && fileName.equals(name))
                        added = foundFiles.add(f);
                    break;
                case MODE_FOLDER_BY_PART:
                    if (f.isDirectory() && fileName.equals(name))
                        added = foundFiles.add(f);
                    break;
                case MODE_ANY_BY_NAME:
                    if (fileName.equals(name))
                        added = foundFiles.add(f);
                    break;
                case MODE_ANY_BY_PART:
                    if (fileName.contains(name))
                        added = foundFiles.add(f);
                    break;
                default:
                    log("Selected search mode is invalid: " + selectedMode + "!!!");
                    System.exit(1);
            }

            if (!recursive && added)
                continue;

            if (f.isDirectory()) {
                List<File> nextFiles = searchInDirectory(f, name);

                if (nextFiles != null)
                    foundFiles.addAll(nextFiles);
            }
        }

        return foundFiles;
    }

    /** Returns list of root directories in Windows. Each element is directory
     * @return List of root directories in Windows
     */
    private List<File> listDrivesWin32() {
        List<File> drives = new LinkedList<>();

        for (char c = 'A'; (int)c <= (int)'Z'; c++) {
            log("Searching for " + c + ":\\");
            File dir = new File(String.valueOf(c) + ":\\");

            if (dir.exists() && c != 'C') { // IGNORING DISK C. TODO: make it work(Check for access right etc)
                log("Directory exists: " + c + ":\\");
                drives.add(dir);
            }
        }

        return drives;
    }

    /** Returns list of root directories in Linux. Each element is directory
     *  TODO: REMAKE IT PLEASE
     * @return List of root directories in Linux
     */
    private List<File> listDrivesLinux() {
        List<File> drives = new LinkedList<>();

        drives.add(new File("/"));

        return drives;
    }
}
