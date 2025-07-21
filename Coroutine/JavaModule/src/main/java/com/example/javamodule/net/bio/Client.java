package com.example.javamodule.net.bio;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = null;
            ObjectInputStream inputStream = null;
            ObjectOutputStream outputStream = null;
            //创建服务器地址
            InetSocketAddress socketAddress =
                    new InetSocketAddress("127.0.0.1", 10001);

            try {
                socket = new Socket();
                //通过地址开始连接
                socket.connect(socketAddress);

                /**
                 * 🌟这里顺序很重要‼️
                 * 必须跟服务器适配，无法会发生死锁
                 * 不能同时接收和同时发送。必须要一个接收，另一个要先发送
                 */
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

                //发送消息
                outputStream.writeUTF("Laureate");
                outputStream.flush();

                //接收
                System.out.println("收到了！！！" + inputStream.readUTF());
            }finally {
                if (socket != null) {
                    socket.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }catch (Exception e){
            System.out.println("异常," + e);
        }

    }
}
