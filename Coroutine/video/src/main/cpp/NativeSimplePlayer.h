//
// Created by 周君瑜 on 2025/7/22.
//

#ifndef COROUTINE_NATIVESIMPLEPLAYER_H
#define COROUTINE_NATIVESIMPLEPLAYER_H

#include <cstring>
#include <pthread.h>
#include "AudioChannel.h"
#include "VideoChannel.h"
#include "JNICallbackHelper.h"
#include "AudioChannel.h"
#include "VideoChannel.h"
#include "util.h"


extern "C"{ //ffmpeg 是纯C写的，必须采用C的编译方式，否则崩溃
#include "libavformat/avformat.h"
#include "libavcodec/avcodec.h"

};

class NativeSimplePlayer {
private:
    char  *data_source = 0;//指针需要初始值
    pthread_t pid_prepare;
    AVFormatContext  *formatContext = 0;
    AudioChannel *audio_channel = 0;
    VideoChannel *video_channel = 0;
    JNICallbackHelper *helper =0 ;
public:
    NativeSimplePlayer(const char *data_source,JNICallbackHelper *helper);

    ~NativeSimplePlayer();

    void prepare();

    void prepare_();
};


#endif //COROUTINE_NATIVESIMPLEPLAYER_H
