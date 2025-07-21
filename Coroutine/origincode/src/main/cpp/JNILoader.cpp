#include <string>
#include "com_example_origincode_jni_JNILoader.h"

JNIEXPORT extern "C" jstring JNICALL Java_com_example_origincode_jni_JNILoader_GetNativeString
        (JNIEnv *env, jobject obj){
    std::string labelString = "Hello native c++ from me.";
    return env->NewStringUTF(labelString.c_str());
}