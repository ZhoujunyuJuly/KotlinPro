package com.example.javamodule.net.nio.server;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOServerGPT {
    public static void main(String[] args) throws Exception {
        // 打开ServerSocketChannel
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(10001));
        serverChannel.configureBlocking(false); // 设置非阻塞

        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("NIO服务器启动，监听端口10001...");

        while (true) {
            selector.select(); // 阻塞直到有事件
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) { // 接收新连接
                    SocketChannel client = serverChannel.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接：" + client.getRemoteAddress());
                } else if (key.isReadable()) { // 可读事件
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len = client.read(buffer);
                    if (len == -1) {
                        client.close();
                        continue;
                    }
                    buffer.flip();
                    String msg = new String(buffer.array(), 0, len);
                    System.out.println("收到消息: " + msg);

                    // 回写
                    client.write(ByteBuffer.wrap(("Hello, " + msg).getBytes()));
                }
            }
        }
    }
}
