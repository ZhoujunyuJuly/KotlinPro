package com.example.javamodule.net.interview.lock;

public abstract class BaseLock {
    private int threadOrder;
    private String name;
    protected int times;
    protected int currentOrder = 0;
    protected int count;

    public BaseLock(int times,int count) {
        this.times = times;
        this.count = count;
    }

    abstract void print(int order,String name);

    @SuppressWarnings("DefaultLocale")
    void printInfo(int threadOrder, String name,int time){
        System.out.println(String.format("线程顺序：%d,线程名称：%s，当前第%d次打印",threadOrder,name,time));
    }

}
