package com.example.javamodule.net.nio.server;


import static com.example.javamodule.net.nio.client.NIOClient.DEFAULT_PORT;

public class NIOServerWritable {
    private static NIOServerHandleWritable serverHandle;

    public static void main(String[] args) {
        serverHandle = new NIOServerHandleWritable(DEFAULT_PORT);
        new Thread(serverHandle,"Server").start();
    }
}
