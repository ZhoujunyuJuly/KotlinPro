/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_example_video_FFmpegLoader */
#include <string>
#include "NativeSimplePlayer.h"
#include "JNICallbackHelper.h"

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libavutil/avutil.h>
}

#ifndef _Included_com_example_video_FFmpegLoader
#define _Included_com_example_video_FFmpegLoader
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_video_FFmpegLoader
 * Method:    getFFmpegVersion
 * Signature: ()V
 */
JNIEXPORT jstring JNICALL Java_com_example_video_SimplePlayer_getFFmpegVersion
  (JNIEnv *env, jobject){
    const char *version = av_version_info();
    return env->NewStringUTF(version);
}

#ifdef __cplusplus
}
#endif
#endif

//虚拟机可以跨线程
JavaVM  *vm = 0;
jint JNI_OnLoad(JavaVM *vm,void *args){
    ::vm = vm;
    return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_video_SimplePlayer_prepareNative(JNIEnv *env, jobject job, jstring data_source) {
    const char* data_source_ = env->GetStringUTFChars(data_source,0);
    //可能是主线程，也可能是子线程
    auto *helper = new JNICallbackHelper(vm,env,job);
    auto *player = new NativeSimplePlayer(data_source_,helper);
    player->prepare();
    env->ReleaseStringUTFChars(data_source,data_source_);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_video_SimplePlayer_startNative(JNIEnv *env, jobject thiz) {
    // TODO: implement startNative()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_video_SimplePlayer_stopNative(JNIEnv *env, jobject thiz) {
    // TODO: implement stopNative()
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_video_SimplePlayer_releaseNative(JNIEnv *env, jobject thiz) {
    // TODO: implement releaseNative()
}