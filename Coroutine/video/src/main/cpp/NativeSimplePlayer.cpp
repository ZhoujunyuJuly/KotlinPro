//
// Created by 周君瑜 on 2025/7/22.
//

#include <string.h>
#include "NativeSimplePlayer.h"

NativeSimplePlayer::NativeSimplePlayer(const char *data_source_,JNICallbackHelper *helper) {
    //如果被释放，会造成悬空指针
    //this->data_source = data_source_;

    //深拷贝
    //C层：demo.mp4\0 C层会自动 + \0 ,strlen 不计算 \0 的长度
    this->data_source = new char[strlen(data_source_) + 1];
    strcpy(this->data_source,data_source_);

    this->helper = helper;
}

NativeSimplePlayer::~NativeSimplePlayer() {
    if( data_source ){
        delete data_source;
    }

    if( helper){
        delete helper;
    }
}

void* task_prepare(void * args){
    //读取文件
    auto *player = static_cast<NativeSimplePlayer *>(args);
    player->prepare_();
    return 0;//必须返回，错误很难找
}

void NativeSimplePlayer::prepare_() {
    //为什么FFmpeg源码，大量使用上下文 Context
    //因为FFmpge源码是纯C，他没有对象，只能上下文贯穿环境，操作成员变量


    /**
     * 第一步，打开媒体文件地址
     * @parm AVFormatContext  -> formatContext 上下文
     * @parm filename         -> data_source 路径
     * @parm AVInputFormat    -> *fmt Mac、Windows 摄像头、麦克风
     * @param AVDictionary    -> 设置Http连接超时，打开rtmp超时
     */
     formatContext = avformat_alloc_context();
     AVDictionary  *dictionary = 0;
     av_dict_set(&dictionary,"timeout","5000000",0);
     int result = avformat_open_input(&formatContext,data_source,0,&dictionary);

     //释放字典
     av_dict_free(&dictionary);

     if(result){
         //回调错误信息给Java，通过JNI反射
         if( helper ){
             //打不开视频，后面的报错以此类推
             helper->onError(THREAD_CHILD,1);
         }
         return;
     }

     /**
      * 第二步，查找媒体中的音视频流信息
      */
    result = avformat_find_stream_info(formatContext,0);
    if( result < 0 ){
        return;
    }

    /**
     * 第三步，根据流信息，流的个数，用循环来找
     */

//    for(int i = 0;i < formatContext->nb_streams;++i){
    for(int i = 0;i < 2;++i){

        /**
         * 第四步，获取媒体流（视频，音频）
         */
         AVStream *stream = formatContext->streams[i];

         /**
          * 第五步，从上面的流中 获取 编码解码的参数
          * 由于后面的解码器 编码器 都需要参数（记录的宽高）
          */
          AVCodecParameters *parameters = stream->codecpar;

          /**
           * 第六步，获取编/解码器，根据上面的参数👆
           */
         const AVCodec *codec = avcodec_find_decoder(parameters->codec_id);

         /**
          * 第七步，编解码器 上下文【真正干活】
          */
          AVCodecContext *codecContext = avcodec_alloc_context3(codec);
          if( !codecContext ){
              return;
          }

          /**
           * 第八步，他目前是一张白纸
           * 把 parameter 拷贝给=> codecContext
           */
        result = avcodec_parameters_to_context(codecContext,parameters);
        if( result < 0){
            return;
        }

        /**
         * 第九步，打开解码器
         */
        result = avcodec_open2(codecContext,codec,0);
        if( result ){
            return;
        }

        /**
         * 第十步，从编解码器从，获取流的类型 codec_type 决定是音频还是视频
         */
        if( parameters->codec_type == AVMediaType::AVMEDIA_TYPE_AUDIO){
            //分开音频
            audio_channel = new AudioChannel();
        } else if(parameters->codec_type == AVMediaType::AVMEDIA_TYPE_VIDEO){
            //分开视频
            video_channel = new VideoChannel();
        }
    }

    /**
     * 第十一步，如果流中没有音频，也没有视频 【健壮性校验】
     */
     if( !audio_channel && !video_channel){
         return;
     }

     /**
      * 第十二步，恭喜你，准备成功，媒体准备完成，通知上层
      */

     if(helper){
         helper->onPrepared(THREAD_CHILD);
     }
}




void NativeSimplePlayer::prepare() {
    //此时为Activity调用到的，所以为主线程

    //解封装 FFmpeg 来解析，要使用子线程
    pthread_create(&pid_prepare,0,task_prepare,this);

}

