package com.popovych;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TelnetServer {
    private static BlockingQueue<SearchTask> searchQueue = new LinkedBlockingQueue<SearchTask>();
    private static FileSearchThread fileSearchThread;

    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        String rootPath = args[1];

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Telnet server started on port " + port);

            fileSearchThread = new FileSearchThread(searchQueue);
            fileSearchThread.start();

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");

                new ClientHandler(rootPath, socket, searchQueue).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
