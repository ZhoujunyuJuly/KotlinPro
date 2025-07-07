package com.example.coroutine.mvvm.mvp;

import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    /**
     * 🈲禁止指令重排
     * 存在一种情况：
     * (1)A线程进入正在执行：1.分配内存空间 2.将内存空间分配给对象 3.构造函数初始化
     * (2)在A完成了引用，但是还未构造器初始化时，此时B线程进入
     * (3)由于存在引用，判断非空，于是返回了未完成初始化的A线程变量
     */
    private volatile RetrofitClient mInstance;

    private Retrofit mRetrofit;

    private RetrofitClient getInstance(){
        /**
         * 第一次枷锁:减少锁开销，避免每一次进入都耗费锁资源
         */
        if( mInstance == null){
            /**
             * 第二次枷锁:
             * (1)没初始化时，所有线程都在锁外面等待
             * (2)A完成初始化后，B又会再来一次，存在同步问题
             */
            synchronized (RetrofitClient.class){
                if(mInstance == null){
                    mInstance = new RetrofitClient();
                }
            }
        }
        return mInstance;
    }


    private <T> T getService(Class<T> clz){
        return getRetrofit().create(clz);
    }



    private synchronized Retrofit getRetrofit(){
        if( mRetrofit == null){
            mRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .baseUrl("")
                    .build();
        }
        return mRetrofit;
    }
}
