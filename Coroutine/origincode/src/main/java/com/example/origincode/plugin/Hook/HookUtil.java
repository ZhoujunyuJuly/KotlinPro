package com.example.origincode.plugin.Hook;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class HookUtil {
    private static final String TAG = "zjy";
    public static final String TARGET_INTENT = "target_intent";


    public static void  hookAMS() throws Exception {
        if( testHook()){
            return;
        }
        /**
         * 🤔我们找到的Hook点
         * public class ActivityManager {
         *     ...
         *     // static 的单例字段，内部是 Singleton<IActivityManager>
         *     private static final Singleton<IActivityManager> IActivityManagerSingleton =
         *         new Singleton<IActivityManager>() {
         *             @Override
         *             protected IActivityManager create() {
         *                 IBinder b = ServiceManager.getService("activity");
         *                 return IActivityManager.Stub.asInterface(b);
         *             }
         *         };
         * }
         */
        Class<?> clz = Class.forName("android.app.ActivityManager");
        Field singletonField = clz.getDeclaredField("IActivityManagerSingleton");
        singletonField.setAccessible(true);
        //🌟因为singleton是static静态变量，如果是非静态变量，就需要传一个对象
        Object singleton = singletonField.get(null);


        //🌟以下操作是为了在反射中调用 mInstance 的方法，不改变系统原有执行流程，因为我只需要方法中的 intent 参数
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        //这里如果要传实例化对象，不能传类 Class<?>
        final Object mInstance = mInstanceField.get(singleton);

        Class<?> iActManClz = Class.forName("android.app.IActivityManager");

        Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{iActManClz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("zjy,hookAMS invoke called for method: " + method.getName());

                if( "startActivity".equals(method.getName())){
                    for (int i = 0; i < args.length; i++) {
                        if( args[i] instanceof Intent ){
                            Intent originIntent = (Intent)args[i];
                            Intent proxyIntent = new Intent();

                            /**
                             * ‼️️这里卡了好久，注意前面是填包名，后面是类名
                             */
                            proxyIntent.putExtra("target_intent",originIntent);
                            proxyIntent.setClassName("com.example.origincode",
                                    "com.example.origincode.plugin.ProxyNewActivity");
                            args[i] = proxyIntent;
                            break;
                        }
                    }
                }

                return method.invoke(mInstance,args);
            }
        });


        mInstanceField.set(singleton,proxyInstance);

    }

    private static boolean testHook () throws Exception {
        Class<?> amClass = Class.forName("android.app.ActivityManager");
        Field singletonField = amClass.getDeclaredField("IActivityManagerSingleton");
        singletonField.setAccessible(true);
        Object singleton = singletonField.get(null);

        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Method getMethod = singletonClass.getDeclaredMethod("get");
        getMethod.setAccessible(true);

        Object iActivityManager = getMethod.invoke(singleton);

        System.out.println("zjy,IActivityManager proxy: " + iActivityManager.getClass().getName());

        return false;
    }

    /**
     * ‼️又是坑人的老师傅版
     *
     * 🌟将 ProxyIntent 转化为 插件化页面
     * Android10 Handler 的消息 obj 也变化了，所以用原来的无法找到对应的消息
     */
    public static void hookHandler() {
        /**
         * 我们找到在 Handler.handleMessage() 处理 LAUNCH_ACTIVITY 消息时，
         * 会将 (ActivityRecord) msg.obj,而这个 ActivityRecord 存有 Intent
         * 🌟所以我们要拿到这个 Intent 替换我们的插件页面
         * 但是要如何拿到这个 msg 呢？
         * 发现在 handleMessage() 回调时，如果实现了 Callback 接口，会传入 msg
         * 于是我们先增加一个 callBack
         */
        Handler.Callback pluginCallback = new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                //msg.what = LAUNCH_ACTIVITY
                Log.d(TAG, "handleMessage: what" + msg.what);
                if( msg.what != 159){
                    return false;
                }

                try {
                    Field intentField = msg.obj.getClass().getDeclaredField("intent");
                    intentField.setAccessible(true);
                    Intent proxyIntent = (Intent)intentField.get(msg.obj);
                    Intent originIntent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                    /**
                     * 怎么理解这个set?
                     * 🌟其实对比取值，field.get(上一层的对象)
                     *   那么如果要重新设置值，也需要再传上一层的对象，然后覆盖
                     * ⏰所以之前 mInstanceField.set(Singleton的对象,要替换的 mInstance)
                     */
                    if( originIntent != null){
                        intentField.set(msg.obj,originIntent);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                //必须return false 要执行默认的 handleMessage() 方法
                return false;
            }
        };

        /**
         * 要让这个增加的 callback 生效到系统中，我们需要对系统 Handler 进行动态代理
         * 必须要找到一个 static 变量，作为我们开始寻找的值，于是按图索骥：
         * Handler -> new mH extends Handler -> ActivityThreads 这个是static 值
         * ⏰开干
         */

        try{
            //获取ActivityThread 对象
            Class<?> threadClz = Class.forName("android.app.ActivityThread");
            Field sCurThreadField = threadClz.getDeclaredField("sCurrentActivityThread");
            sCurThreadField.setAccessible(true);
            Object activityThread = sCurThreadField.get(null);

            //获取 mH 对象
            Field mHField = threadClz.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler)mHField.get(activityThread);

            Field callbackField = Handler.class.getDeclaredField("mCallback");
            callbackField.setAccessible(true);

            //替换系统的 callback
            callbackField.set(mH,pluginCallback);
        }catch (Exception e){
            Log.d(TAG, "hookHandler: " + e);
        }
    }


}
