package com.popovych;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final String rootPath;
    private final BlockingQueue<SearchTask> searchQueue;


    public ClientHandler(String rootPath, Socket socket, BlockingQueue<SearchTask> searchQueue) {
        this.rootPath = rootPath;
        this.socket = socket;
        this.searchQueue = searchQueue;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            out.println("For search enter {depth} and {mask}, for exit enter 'exit'");

            String line;
            while ((line = in.readLine()) != null) {
                if (line.equalsIgnoreCase("exit")) {
                    out.println("Bye");
                    break;
                }

                String[] parts = line.split(" ");

                if (parts.length != 2) {
                    out.println("Invalid input format. Please provide search path and mask separated by space.");
                    continue;
                }
                int depth = Integer.parseInt(parts[0]);
                String mask = parts[1];

                SearchTask task = new SearchTask(rootPath, depth, mask, out, null);
                searchQueue.put(task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
