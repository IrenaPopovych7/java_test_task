package com.popovych;

import java.util.concurrent.BlockingQueue;

public class FileSearchThread extends Thread {
    private final BlockingQueue<SearchTask> searchQueue;

    public FileSearchThread(BlockingQueue<SearchTask> searchQueue) {
        this.searchQueue = searchQueue;
    }

    public void run() {
        while (true) {
            try {
                SearchTask task = searchQueue.take();
                SearchTask nextTask = task.execute();
                if (nextTask != null) {
                    searchQueue.put(nextTask);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
