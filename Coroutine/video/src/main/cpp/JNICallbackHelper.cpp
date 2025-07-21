//
// Created by 周君瑜 on 2025/7/22.
//

#include "JNICallbackHelper.h"

JNICallbackHelper::JNICallbackHelper(_JavaVM *vm, _JNIEnv *env, jobject jobject) {
    this->vm = vm;
    this->env = env;
    //全局引用
    this->job = env->NewGlobalRef(jobject);
    jclass claz = env->GetObjectClass(jobject);
    //🌟这里没有赋值
    jmd_prepared = env->GetMethodID(claz,"onPrepared","()V");
    jmd_error = env->GetMethodID(claz,"onError","(I)V");
}

JNICallbackHelper::~JNICallbackHelper() {
    vm = 0;
    env->DeleteGlobalRef(job);
    job = 0;
    env = 0;
}

void JNICallbackHelper::onPrepared(int thread_mode) {
    if( thread_mode == THREAD_MAIN){
        env->CallVoidMethod(job,jmd_prepared);
    } else if (thread_mode == THREAD_CHILD){
        //子线程不可以跨线程，要用全新的env
        JNIEnv  *env_child;
        vm->AttachCurrentThread(&env_child,0);
        env_child->CallVoidMethod(job,jmd_prepared);
        vm->DetachCurrentThread();
    }
}


void JNICallbackHelper::onError(int thread_mode,int error_code) {
    if( thread_mode == THREAD_MAIN){
        env->CallVoidMethod(job,jmd_error);
    } else if (thread_mode == THREAD_CHILD){
        //子线程不可以跨线程，要用全新的env
        JNIEnv  *env_child;
        vm->AttachCurrentThread(&env_child,0);
        env_child->CallVoidMethod(job,jmd_error,error_code);
        vm->DetachCurrentThread();
    }
}
