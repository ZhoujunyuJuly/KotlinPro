package com.example.javamodule.net.interview.lock;

/**
 * 同步锁
 */
public class SynchronizedLock extends BaseLock{

    public SynchronizedLock(int times,int count) {
        super(times,count);
    }

    public synchronized void print(int order,String name){
        for (int i = 0; i < times; i++) {
            while (currentOrder != order){
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            currentOrder = (currentOrder + 1) % count;
            printInfo(order,"SynchronizedLock" + name,i);
            notifyAll();
        }
    }
}
