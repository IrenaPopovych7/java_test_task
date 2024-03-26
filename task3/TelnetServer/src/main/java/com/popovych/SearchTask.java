package com.popovych;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;

public class SearchTask {
    private final String rootPath;
    private final int depth;
    private final String mask;
    private final PrintWriter out;
    private final Deque<FileDepthPair> fileDepthPairDeque;

    public SearchTask(String rootPath, int depth, String mask, PrintWriter out, Deque<FileDepthPair> fileDepthPairDeque) {
        this.rootPath = rootPath;
        this.depth = depth;
        this.mask = mask;
        this.out = out;
        this.fileDepthPairDeque = fileDepthPairDeque;
    }

    public SearchTask execute() {
        return search(rootPath, depth, mask, out, fileDepthPairDeque);
    }

    private static SearchTask search(String rootPath, int depth, String mask, PrintWriter out, Deque<FileDepthPair> fileDepthPairDeque) {
        Deque<FileDepthPair> queue;
        if (fileDepthPairDeque == null) {
            queue = new ArrayDeque<>();
            queue.add(new FileDepthPair(new File(rootPath), 0));
        } else {
            queue = fileDepthPairDeque;
        }

        FileDepthPair pair = queue.poll();
        if (pair == null) {
            return null;
        }

        File file = pair.getFile();
        int currentDepth = pair.getDepth();

        if (currentDepth <= depth) {
            if (file.getName().contains(mask)) {
                out.println(file);
            }

            if (file.isDirectory()) {
                try {
                    for (File child : file.listFiles()) {
                        if (child != null)
                            queue.add(new FileDepthPair(child, currentDepth + 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return new SearchTask(rootPath, depth, mask, out, queue);
        } else {
            return null;
        }
    }
}