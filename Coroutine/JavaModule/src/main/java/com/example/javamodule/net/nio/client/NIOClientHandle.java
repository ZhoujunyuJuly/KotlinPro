package com.example.javamodule.net.nio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOClientHandle implements Runnable{
    private String ip;
    private int port;
    private static volatile boolean started;
    private SocketChannel socketChannel;
    private Selector selector;

    public NIOClientHandle(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            started = true;
        } catch (IOException e) {
            System.out.println("NIOClientHandle()->" + e);
        }
    }

    public void stop(){
        started = false;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            System.out.println("连接失败，" + e);
            System.exit(1);
        }

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

    private void doConnect() throws Exception {
        //connect 为非阻塞，当他返回时，连接不一定完成了
        //如果返回为true，可以开始读取
        //如果返回为false，连接没完成，还在三次握手中，继续连接
        if( socketChannel.connect(new InetSocketAddress(ip,port))){
            socketChannel.register(selector,SelectionKey.OP_READ);
        }else {
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void handelInput(SelectionKey key) {
        try {
            //🌟为什么客户端不会有问题？
            //‼️客户端使用 SocketChannel.open() 连接服务器，注册到 Selector 上的通道就是 SocketChannel。
            // 客户端只会关注 OP_CONNECT、OP_READ、OP_WRITE 等事件
            // 不会出现 OP_ACCEPT 事件（OP_ACCEPT 只出现在服务端监听 ServerSocketChannel 时）。
            if(key.isValid()){
                SocketChannel channel = (SocketChannel)key.channel();

                if(key.isConnectable()){
                    if( channel.finishConnect()){
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }else {
                        //❌说明连接失败，直接退出
                        System.exit(1);
                    }
                }

                //读数据
                if(key.isReadable()){
                    //创建ByteBuffer，开辟一个1M缓冲区
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int readBytes = channel.read(byteBuffer);

                    if( readBytes > 0){
                        //切换为缓冲区的读操作
                        byteBuffer.flip();
                        //根据缓冲区可读字节数，创建字节数组
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        //从 ByteBuffer 中将数据取出，写入到 bytes 数组中
                        byteBuffer.get(bytes);

                        String result = new String(bytes);
                        System.out.println("客户端收到消息:" + result);
                    }else if( readBytes < 0){
                        //释放资源
                        key.cancel();
                        channel.close();
                    }
                }
            }


        }catch (Exception e){
            System.out.println("handelInput()->异常," + e);
        }
    }

    public void sendMsg(String msg) throws Exception{
        doWrite(socketChannel,msg);
    }

    private void doWrite(SocketChannel channel,String request) throws  Exception{
        byte[] bytes = request.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        //🌟要先将字节数组复制到缓冲区，这时候不需要写模式
        //🧊只有用到channel 的 write 操作时，要切换写模式
        byteBuffer.flip();
        channel.write(byteBuffer);

    }
}
