package com.example.javamodule.net.interview.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyReentrantLock extends BaseLock{

    private ReentrantLock lock = new ReentrantLock();
    private Condition conditions = lock.newCondition();
    private int order;

    public MyReentrantLock(int times, int count) {
        super(times, count);
    }

    @Override
    void print(int threadOrder, String name) {
        lock.lock();
        for (int i = 0; i < times; i++) {
            try {
                while (order != threadOrder){
                    conditions.await();
                }
            } catch (Exception e) {
                System.out.println("异常" + e);
                lock.unlock();
                throw new RuntimeException(e);
            }

            order = (order + 1)%count;
            printInfo(threadOrder,"MyReentrantLock" + name,i);
            conditions.signalAll();
        }
        lock.unlock();
    }
}
