package com.example.origincode;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.origincode", appContext.getPackageName());
    }


    @Test
    public void useHandlerThread() throws Exception{
        HandlerThread handlerThread = new HandlerThread("my-success");
        handlerThread.start();

        Handler task = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                System.out.println("接收消息的线程：" + Thread.currentThread().getName() + " ,what = " + msg.what );
            }
        };

        task.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行消息的线程：" + Thread.currentThread().getName()  );
            }
        });
        task.sendEmptyMessage(100);
        System.out.println("发送消息的线程：" + Thread.currentThread());
        Thread.sleep(1000);
        handlerThread.quitSafely();
    }
}