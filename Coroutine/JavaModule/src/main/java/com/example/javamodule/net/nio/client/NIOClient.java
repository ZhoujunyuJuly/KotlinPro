package com.example.javamodule.net.nio.client;

import java.util.Scanner;

public class NIOClient {
    public static final String DEFAULT_IP = "127.0.0.1";
    public static final int DEFAULT_PORT = 888;

    private static NIOClientHandle clientHandle;

    public static void start(){
        clientHandle = new NIOClientHandle(DEFAULT_IP,DEFAULT_PORT);
        new Thread(clientHandle,"client").start();
    }

    public static boolean sendMsg(String msg) throws Exception {
        clientHandle.sendMsg(msg);
        return true;
    }

    public static void main(String[] args) throws Exception {
        start();
        Scanner scanner = new Scanner(System.in);
        while (NIOClient.sendMsg(scanner.next()));
    }
}
