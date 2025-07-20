package com.example.javamodule.net.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 老师跟写✍️
 */
public class NIOServerHandle implements Runnable {

    private volatile boolean started = false;
    private Selector selector;
    private ServerSocketChannel serverChannel;

    /**
     * 指定要监听的端口号
     * @param port
     */
    public NIOServerHandle(int port) {
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
        // 🌟别忘了，自己写成 isAcceptable()了
        //1. 🧊校验是否有效性
        if( key.isValid()){
            try {
                if(key.isAcceptable()){
                    //只有是 acceptable 才可以转换成 ServerSocketChannel
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    //🌟别忘了，自己写的时候直接拿channel开张了，法克
                    //🧊连接和客户端通信的 socket
                    SocketChannel socket = channel.accept();
                    //设置不阻塞
                    socket.configureBlocking(false);
                    socket.register(key.selector(),SelectionKey.OP_READ);
                }

                //2.🧊读数据
                if(key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    //用来存放从通道读取的数据
                    //ByteBuffer 是 NIO 中的数据缓冲区，数据会先写入缓冲区，再从缓冲区读取
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    //🌟从 SocketChannel 读取数据并【写入】 byteBuffer
                    int readBytes = channel.read(byteBuffer);

                    //返回值 readBytes 表示实际读取的字节数：
                    //> 0：读取到了数据。
                    //= 0：通道没有数据可读（非阻塞模式下很常见）。
                    //-1：表示对端（客户端）关闭了连接。
                    if( readBytes > 0){
                        //🌟将 ByteBuffer 从写模式切换到【读模式】
                        //🧊flip() 的作用是把 position 置 0，把 limit 设置为之前写入数据的位置，使得可以从缓冲区头部读取数据。
                        byteBuffer.flip();
                        //创建一个新的字节数组，用来存放刚刚读取的数据，大小与读取到的字节数相等
                        byte[] bytes = new byte[readBytes];
                        //🌟从 ByteBuffer 中将数据取出，写入到 bytes 数组中
                        byteBuffer.get(bytes);

                        String message = new String(bytes);
                        System.out.println("服务器收到消息:" + message);
                        String result = "Hello,my baby " + message;
                        //发送应答消息
                        doWrite(channel,result);
                    }else if( readBytes < 0){
                        //释放资源
                        key.cancel();
                        channel.close();
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
            //🌟将 bytes 写入到 buffer 中
            byteBuffer.put(bytes);
            //🌟要开始写入到 channel 了，所以要改成【写模式】
            byteBuffer.flip();
            channel.write(byteBuffer);
        } catch (IOException e) {
            System.out.println("doWrite()->异常," + e);
        }
    }
}
