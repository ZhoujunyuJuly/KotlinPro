//
// Created by 周君瑜 on 2025/7/22.
//

#ifndef COROUTINE_JNICALLBACKHELPER_H
#define COROUTINE_JNICALLBACKHELPER_H


#include <jni.h>
#include "util.h"


class JNICallbackHelper {
private:
    JavaVM *vm = 0;
    JNIEnv *env = 0;
    jobject job;
    jmethodID jmd_prepared;
    jmethodID jmd_error;

public:
    JNICallbackHelper(_JavaVM *vm, _JNIEnv *env, jobject jobject);

    virtual ~JNICallbackHelper();

    void onPrepared(int thread_mode);

    void onError(int thread_mode,int errorCode);

};


#endif //COROUTINE_JNICALLBACKHELPER_H
