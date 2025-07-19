package com.example.origincode.plugin.Hook;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class HookUtil2 {

    private static final String TAG = "zjy";
    public static final String TARGET_INTENT = "target_intent";

    /**
     * 🌟最大的收获是要学习自己看源码‼️
     * 安卓每个版本升级，就意味着极度依赖反射调用的插件化，很多方法都面临改变和升级
     * 必须做好适配，要去看每个方法是否还是这样调用
     */
    public static void hookAMS() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Android 10+ 使用 ActivityTaskManager
                hookSingleton(
                        "android.app.ActivityTaskManager",
                        "IActivityTaskManagerSingleton",
                        "android.app.IActivityTaskManager"
                );
            } else {
                // Android 9 及以下使用 ActivityManager
                hookSingleton(
                        "android.app.ActivityManager",
                        "IActivityManagerSingleton",
                        "android.app.IActivityManager"
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Hook AMS failed", e);
        }
    }

    private static void hookSingleton(String className, String fieldName, String interfaceName) throws Exception {
        // 1. 获取类与字段
        Class<?> clz = Class.forName(className);
        Field singletonField = clz.getDeclaredField(fieldName);
        singletonField.setAccessible(true);
        Object singleton = singletonField.get(null);

        if (singleton == null) {
            throw new IllegalStateException("Hook失败: " + className + "." + fieldName + " is null");
        }

        /**
         *  2. 调用 get() 以初始化 mInstance
         *  🧠Android29之后，Singleton 使用了懒加载，所以直接拿 mInstance 会为空，要通过 get() 调用一下
         */
        Method getMethod = singleton.getClass().getMethod("get");
        final Object mInstance = getMethod.invoke(singleton);

        if (mInstance == null) {
            throw new IllegalStateException("zjy,报错了mInstance is null in singleton: " + singleton);
        }

        // 3. 获取接口类
        Class<?> iManagerInterface = Class.forName(interfaceName);

        // 4. 创建代理对象
        Object proxyInstance = Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                new Class[]{iManagerInterface},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Log.d(TAG, "Hooked method: " + method.getName());

                        if ("startActivity".equals(method.getName())) {
                            Log.d(TAG, "拦截到了 startActivity，可在这里替换 Intent");
                            // 你可以替换 intent，例如 args[2] 是 Intent
                            for (int i = 0; i < args.length; i++) {
                                if( args[i] instanceof Intent ){
                                    Intent originIntent = (Intent)args[i];
                                    Intent proxyIntent = new Intent();

                                    /**
                                     * ‼️️这里卡了好久，注意前面是填包名，后面是类名
                                     */
                                    proxyIntent.putExtra(TARGET_INTENT,originIntent);
                                    proxyIntent.setClassName("com.example.origincode",
                                            "com.example.origincode.plugin.ProxyNewActivity");
                                    args[i] = proxyIntent;
                                    Log.d(TAG, "替换完成 i =" + i);
                                    break;
                                }
                            }
                        }

                        return method.invoke(mInstance, args);
                    }
                });

        // 5. 设置代理对象
        Field mInstanceField = singleton.getClass().getSuperclass().getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        mInstanceField.set(singleton, proxyInstance);

        Log.i(TAG, "hookAMS success for " + className);
    }

    /**
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
                    // msg.obj 是 ClientTransaction 对象，反射拿到它的 getCallbacks 方法
                    Object clientTransaction = msg.obj;
                    Class<?> clientTransactionClass = clientTransaction.getClass();

                    // getCallbacks() 返回 List<ClientTransaction.Item>
                    Method getCallbacksMethod = clientTransactionClass.getDeclaredMethod("getCallbacks");
                    getCallbacksMethod.setAccessible(true);
                    List<?> callbacks = (List<?>) getCallbacksMethod.invoke(clientTransaction);

                    if (callbacks == null) {
                        return false;
                    }
                    /**
                     * 放弃了，就这样吧。仁至义尽了
                     */
                    for (Object item : callbacks) {
                        Class<?> itemClass = item.getClass();
                        String itemClassName = itemClass.getName();

                        // 找 LaunchActivityItem
                        if (itemClassName.endsWith("LaunchActivityItem")) {
                            // 获取当前 Intent
                            Field intentField = item.getClass().getDeclaredField("mIntent");
                            intentField.setAccessible(true);
                            Intent intent = (Intent) intentField.get(item);

                            // 创建你的新 Intent
                            Intent originIntent = intent.getParcelableExtra(TARGET_INTENT);

                            // 替换为新 Intent
                            intentField.set(item, originIntent);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                // 处理 Android 10+ 的 mStartParams
                                Field startParamsField = item.getClass().getDeclaredField("mStartParams");
                                startParamsField.setAccessible(true);
                                Object startParams = startParamsField.get(item);

                                if (startParams != null) {
                                    Class<?> paramsClass = Class.forName("android.app.servertransaction.ActivityStartParams");
                                    Field paramsIntentField = paramsClass.getDeclaredField("mIntent");
                                    paramsIntentField.setAccessible(true);

                                    // 替换 mStartParams 中的 Intent
                                    paramsIntentField.set(startParams, originIntent);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    Log.d(TAG, "handleMessage: " + e);
                }
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
