package com.popovych;

import java.io.File;

public class FileDepthPair {
    private final File file;
    private final int depth;

    public FileDepthPair(File file, int depth) {
        this.file = file;
        this.depth = depth;
    }

    public File getFile() {
        return file;
    }

    public int getDepth() {
        return depth;
    }
}
