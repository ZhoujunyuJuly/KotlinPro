package com.example.javamodule.net.interview.lock.exe;

public class MainPrint {

    public static final int THREAD_COUNT = 3;
    public static final int PRINT_TIMES = 3;

    public static void main(String[] args) {
        Print print = new Print();
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int order = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    print.print(order);
                }
            }).start();
        }
    }

    public static class Print{
        int order;

        public synchronized void print(int threadOrder) {
            for (int i = 0; i < PRINT_TIMES; i++) {
                while ( order != threadOrder){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("当前线程" + threadOrder + "打印次数：" + i);
                order = (order + 1)% THREAD_COUNT;
                notifyAll();
            }
        }
    }


}
