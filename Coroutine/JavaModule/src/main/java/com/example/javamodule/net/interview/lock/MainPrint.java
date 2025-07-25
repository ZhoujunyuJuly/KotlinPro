package com.example.javamodule.net.interview.lock;

/**
 * 多个线程依次打印
 *
 * 核心要点：
 * 1.🌟线程之间需要互相知道彼此状态的前提：共用同一个对象的同一个方法，并对这个方法加锁
 * 2.🌟如何满足执行顺序？
 *    这个对象内部维护一个序列
 *    - 只有满足这个序列时才能获得锁。
 *    - 处理完成后对这个序列递增，交由下一个线程执行。
 *
 */
public class MainPrint {

    public static final int SYNCHRONIZED = 0;
    public static final int REENTRANT_LOCK = 1;

    public static int threadCount = 5;
    public static int times = 3;

    public static void main(String[] args) {

        BaseLock lock = getLock(REENTRANT_LOCK);

        for (int i = 0; i < threadCount; i++) {
            final int order = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lock.print(order,"线程" + order);
                }
            }).start();
        }
    }

    public static BaseLock getLock(int type){
        switch (type){
            case SYNCHRONIZED:
                return new SynchronizedLock(times,threadCount);
            default:
                return new MyReentrantLock(times,threadCount);
        }
    }
}
