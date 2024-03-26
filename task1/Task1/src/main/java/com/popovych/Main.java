package com.popovych;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    private static final int ARG_COUNTS = 3;
    public static void main(String[] args) {

        if (args.length != ARG_COUNTS) {
            System.out.println("Usage: java Main {rootPath} {depth} {mask}");
            return;
        }

        String rootPath = args[0];
        int depth = Integer.parseInt(args[1]);
        String mask = args[2];

        Deque<FileDepthPair> queue = new ArrayDeque<>();
        queue.add(new FileDepthPair(new File(rootPath), 0));

        while (!queue.isEmpty()) {
            FileDepthPair pair = queue.poll();
            File file = pair.getFile();
            int currentDepth = pair.getDepth();

            // Stop further exploration beyond the specified depth
            if (currentDepth > depth) {
                continue;
            }

            if (file.getName().contains(mask)) {
                    System.out.println(file.getAbsolutePath());
            }

            // Explore only if current depth is less than specified depth
            if (currentDepth < depth && file.isDirectory()) {
                for (File child : file.listFiles()) {
                    queue.add(new FileDepthPair(child, currentDepth + 1));
                }
            }
        }
    }

    private static class FileDepthPair {
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
}