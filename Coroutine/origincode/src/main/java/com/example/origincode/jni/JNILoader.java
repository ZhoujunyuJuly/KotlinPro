package com.example.origincode.jni;


public class JNILoader {
    /**
     * 1.🌟写一个native方法
     * @return
     */

    public native String GetNativeString();
    /**
     * 1.并且加载JNI
     */
    static {
        System.loadLibrary("MyLoader");
    }

    /**
     * 3.🌟编译 JNILoader
     * - 在当前项目下编译本 JNILoader.java 文件，生成 JNILoader.class 文件
     * 🎒javac com/example/origincode/jni/JNILoader.java
     * - 编译当前 JNILoader.java 文件，在系统jni文件夹下，生成 .h 头文件
     * 🎒javac -h ./jni com.example.origincode.jni.JNILoader.java
     *
     *
     * 4.🌟移动 .h 文件夹至 cpp文件夹下
     *   🌟在cpp文件夹下，创建一个 JNILoader.cpp 文件
     *   - 导入 .h 文件头
     *   - 实现 .h 文件头内的方法
     *
     * #include <string>
     * #include "com_example_origincode_jni_JNILoader.h"
     *
     * JNIEXPORT jstring JNICALL Java_com_example_origincode_jni_JNILoader_GetNativeString
     *         (JNIEnv *env, jobject obj){
     *     std::string labelString = "Hello native c++ from me.";
     *     return env->NewStringUTF(labString.c_str());
     * }
     *
     *
     * 5.🌟在 origincode 模块目录下创建一个 CMakeLists.txt 文件
     * 写入：
     *   - CMake 版本
     *   - 链接的 cpp 动态库
     *   - cpp 的日志库
     *
     * cmake_minimum_required(VERSION 3.6)
     * add_library(JNILoader SHARED src/main/cpp/JNILoader.cpp)
     * find_library(log-lib log)
     * target_link_libraries(JNILoader ${log-lib})
     *
     *
     * 6.🌟在 build.gradle 文件里，添加 native
     *  - 在 android{ defaultConfig { 添加 externalNativeBuild { c++编译器}}}
     *  - 在 android {externalNativeBuild{ 添加 cmake 路径}}
     *
     *
     *  android {
     *    🧠 defaultConfig {
     *         externalNativeBuild {
     *             cmake {
     *                 cppFlags("")
     *             }
     *         }
     *     }
     *    🧠 externalNativeBuild{
     *         cmake{
     *             path = file("CMakeLists.txt")
     *         }
     *     }
     * }
     *
     * 7.🌟 build 整个项目
     *   可以在 build/intermediates/cxx/Debug/6u13206n/obj/ 目录
     *   看到 多种 armeabi-v7a/arm64-v8a/x86 等多个 .so 文件
     *   - 其中 armeabi-v7a 支持2011年之后所有安卓手机
     *
     *   就可以直接跑项目了
     *
     *
     *  8.🌟修改一次不需要跑那么多 .so 文件
     *
     *  android {
     *     defaultConfig {
     *        🧠 ndk{
     *             abiFilters += listOf("armeabi-v7a","x86_64")
     *         }
     *     }
     * }
     */
}
