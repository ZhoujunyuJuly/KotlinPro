package com.example.origincode.plugin.Hook;

import android.content.Context;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class PluginApk {

    public static void loadApk(Context context) throws Exception {
        /**
         * 🌟首先通过反射拿到的 BaseDexClassLoader/DexPathList都只是一个类，非对象
         *   所以反射拿到该类之后，就像是 DexPathList.class 可以多次给宿主和插件进行反射调用
         * 💊查找流程如下，要找到 dexElements，需要从 BaseDexClassLoader 找到成员变量，该成员变量类中的成员变量是 dexElements
         *  BaseDexClassLoader
         *     └── pathList: DexPathList
         *        └── dexElements: Element[]
         */
        Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
        Field pathListField = clazz.getDeclaredField("pathList");
        pathListField.setAccessible(true);


        Class<?> dexClz = Class.forName("dalvik.system.DexPathList");
        Field dexField = dexClz.getDeclaredField("dexElements");
        dexField.setAccessible(true);

        /**
         * 🌟究竟怎么取值呢？
         * 首先，一个包中的任何一个类，它的顶层类都是 BaseDexClassLoader
         *     由此我们可以得到当前包的 BaseDexClassLoader 对象
         * 其次，pathListField.get(getClassLoader()) 这句的含义是
         *     我要获取 getClassLoader()-》也就是 BaseDexClassLoader 的某个变量/方法
         *     究竟是什么呢？从 pathListField 得知，它是一个叫 pathList 的成员变量。
         *     👉所以 目标内容.get(已有的顶层类)
         * 最后，同样的方法，我们需要找到 dexField -》 也就是叫 dexElements 的成员变量，
         *     👉在哪里找呢？
         *     从刚才获取的 pathList(类型:DexPathList.class) 中找，就得到 elements
         *
         */
        //宿主的类加载器
        Object hostPathList = pathListField.get(context.getClassLoader());
        Object[] hostDexElements = (Object[])dexField.get(hostPathList);

        //插件的类加载器
        String path = context.getExternalFilesDir(null).getAbsolutePath() + "/plugin-debug.apk";
        ClassLoader dexClassLoader = new DexClassLoader(path,context.getCacheDir().getAbsolutePath(),
                null,context.getClassLoader());
        Object pluginPathList = pathListField.get(dexClassLoader);
        Object[] pluginDexElements = (Object[]) dexField.get(pluginPathList);


        /**
         * 传入数组类型与数量，复制数组
         */
        Object[] newDexElements = (Object[]) Array.newInstance(hostDexElements.getClass().getComponentType(),
                hostDexElements.length + pluginDexElements.length);
        System.arraycopy(hostDexElements,0,newDexElements,
                0,hostDexElements.length);
        System.arraycopy(pluginDexElements,0,newDexElements,
                hostDexElements.length,pluginDexElements.length);

        /**
         * 赋值的含义
         * 我要将一个 dexField(叫 dexElements 的成员变量)
         * set(obj,value)
         *      👉obj：目标对象（要赋值的对象）
         *      👉value：要设置的值
         */
        dexField.set(hostPathList,newDexElements);
    }
}
