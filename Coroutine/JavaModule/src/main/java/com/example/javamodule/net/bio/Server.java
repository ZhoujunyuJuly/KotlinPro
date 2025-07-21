package com.example.javamodule.net.bio;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            //我们服务端在哪个端口监听
            serverSocket.bind(new InetSocketAddress(10001));
            try {
                while (true){
                    System.out.println("开始循环======");
                    //accept只负责三次握手，🌟是阻塞式的
                    Socket socket = serverSocket.accept();
                    System.out.println("握手完毕======");
                    /**
                     * 应该优化为线程池方式 BIO
                     * 可用的 CPU 核心数，并将这个数值乘以 2
                     */
                    ExecutorService executor = Executors.newFixedThreadPool(
                            Runtime.getRuntime().availableProcessors() * 2);
                    executor.execute(new ServerTask(socket));

                    //旧方法
//                    new Thread(new ServerTask(socket)).start();
                }
            }finally {
                serverSocket.close();
            }
        }catch (Exception e){
            System.out.println("服务端异常0，" + e);
        }
    }

    private static class ServerTask implements Runnable{
        private Socket socket = null;

        public ServerTask(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (ObjectInputStream inputStream =
                         new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream outputStream =
                    new ObjectOutputStream(socket.getOutputStream()))
            {
                /**
                 * 接收客户端输出，也就是服务器输入
                 */
                String userName = inputStream.readUTF();
                System.out.println("准备接收！！！！Accetp client Message :" + userName);

                /**
                 * 服务端输出，强制刷新
                 */
                outputStream.writeUTF("Hello , " + userName);
                outputStream.flush();

            }catch (Exception e){
                System.out.println("服务端异常1，" + e);
            }finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("服务端异常2，" + e);
                }
            }
        }
    }
}
