package com.example.javamodule.net.nio.server;


import static com.example.javamodule.net.nio.client.NIOClient.DEFAULT_PORT;

public class NIOServer {
    private static NIOServerHandle serverHandle;

    public static void main(String[] args) {
        serverHandle = new NIOServerHandle(DEFAULT_PORT);
        new Thread(serverHandle,"Server").start();
    }
}
