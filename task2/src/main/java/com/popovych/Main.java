package com.popovych;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            System.out.println("Usage: java Main <rootPath> <depth> <mask>");
            return;
        }

        String rootPath = args[0];
        int depth = Integer.parseInt(args[1]);
        String mask = args[2];

        BlockingQueue<File> filesQueue = new LinkedBlockingQueue<>();
        //set up at server side
        Thread searchThread = new Thread(() -> search(rootPath, depth, mask, filesQueue));
        searchThread.start();

        // Client
        Thread printThread = new Thread(() -> printResults(filesQueue));
        printThread.start();

    }

    private static void search(String rootPath, int depth, String mask, BlockingQueue<File> filesQueue) {
        Deque<FileDepthPair> queue = new ArrayDeque<>();
        // Start counting from level 1
        queue.add(new FileDepthPair(new File(rootPath), 0));

        while (!queue.isEmpty()) {
            FileDepthPair pair = queue.poll();
            File file = pair.getFile();
            int currentDepth = pair.getDepth();

            // Stop further exploration beyond the specified depth
            if (currentDepth > depth) {
                continue;
            }

            // Put found file into the queue
            if (file.getName().contains(mask)) {
                try {
                    filesQueue.put(file);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (currentDepth < depth && file.isDirectory()) {
                for (File child : file.listFiles()) {
                    // Increment depth for child directories
                    queue.add(new FileDepthPair(child, currentDepth + 1));
                }
            }
        }
    }



    private static void printResults(BlockingQueue<File> filesQueue) {
        try {
            while (true) {
                File file = filesQueue.take();
                System.out.println(file.getAbsolutePath());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static class FileDepthPair {
        private File file;
        private int depth;

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
