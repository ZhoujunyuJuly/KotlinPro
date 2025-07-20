package com.example.javamodule.net.nio.server;

import java.awt.RenderingHints;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 老师跟写✍️
 */
public class NIOServerHandleWritable implements Runnable {

    private volatile boolean started = false;
    private Selector selector;
    private ServerSocketChannel serverChannel;

    /**
     * 指定要监听的端口号
     * @param port
     */
    public NIOServerHandleWritable(int port) {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            //🌟设置false，表示当前通道为非阻塞模式，不同于accept会阻塞
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            //注册OP_ACCEPT事件，表明serverChannel 关注的是 accept 连接事件
            serverChannel.register(selector,SelectionKey.OP_ACCEPT);
            started = true;
            System.out.println("服务已启动");

        } catch (IOException e) {
            System.out.println("启动异常 ," + e);
        }

    }

    @Override
    public void run() {
        while (started){
            try {
                //每1s唤醒一次，如果有事件也会被唤醒
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    //避免下次事件会被重复处理
                    it.remove();
                    try {
                        handelInput(key);
                    }catch (Exception e){
                        if( key != null){
                            key.cancel();
                            if( key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("异常1 run()," + e);
            }
        }

        if( selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                System.out.println("异常2 run()," + e);
            }
        }
    }

    private void handelInput(SelectionKey key) {
        if( key.isValid()){
            try {
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();

                    SocketChannel socket = channel.accept();
                    socket.configureBlocking(false);
                    socket.register(key.selector(),SelectionKey.OP_READ);
                }

                if(key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int readBytes = channel.read(byteBuffer);

                    if( readBytes > 0){
                        byteBuffer.flip();
                        byte[] bytes = new byte[readBytes];
                        byteBuffer.get(bytes);

                        String message = new String(bytes);
                        System.out.println("服务器收到消息:" + message);
                        String result = "Hello,my baby " + message;
                        doWrite(channel,result);
                    }else if( readBytes < 0){
                        key.cancel();
                        channel.close();
                    }
                }

                if( key.isWritable()){
                    System.out.println("写事件....");
                    SocketChannel channel = (SocketChannel)key.channel();
                    //🌟从这里取出 channel 携带的 byteBuffer
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    if( byteBuffer.hasRemaining()){
                        int count = channel.write(byteBuffer);
                        System.out.println("写事件->" + count);
                    }else {
                        //🌟只要关心读事件了，取消写事件
                        //‼️因为只要注册了写事件，当写缓存为空时，会一直通知服务器可以写了
                        //导致一直会循环到这里。所以写完了要取消
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }catch (Exception e){
                System.out.println("handelInput()->异常," + e);
            }
        }
    }

    private void doWrite(SocketChannel channel, String result) {
        try {
            byte[] bytes = result.getBytes();
            ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
            byteBuffer.put(bytes);
            byteBuffer.flip();
            //🌟这里注册一个写事件，并传入byteBuffer
            //🌟这是使用了操作系统内核管理写事件，隔壁那种是应用自己管理的写
            channel.register(selector,SelectionKey.OP_WRITE
                    | SelectionKey.OP_READ,byteBuffer);
        } catch (IOException e) {
            System.out.println("doWrite()->异常," + e);
        }
    }
}
