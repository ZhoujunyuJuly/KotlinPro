cmake_minimum_required(VERSION 3.6)
project(ffmpeg_test)

# 添加头文件目录
include_directories(${CMAKE_SOURCE_DIR}/include)

# 引入 FFmpeg 的 so 库
# 1.编解码器核心库
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jni/${ANDROID_ABI}/libavcodec.so)

#2.负责封装/解封装（容器格式，如 mp4、mkv）
add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jni/${ANDROID_ABI}/libavformat.so)

#3.工具库（数学、字节序、日志等）
add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jni/${ANDROID_ABI}/libavutil.so)

#4.视频像素格式转换（YUV ↔ RGB 等）
add_library(swscale SHARED IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jni/${ANDROID_ABI}/libswscale.so)

#5.音频重采样、通道布局转换
add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jni/${ANDROID_ABI}/libswresample.so)

#6.滤镜处理（裁剪、特效等）
add_library(avfilter SHARED IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../jni/${ANDROID_ABI}/libavfilter.so)


# 添加你的本地库
add_library(native-lib-myFFmpeg
        SHARED
        native-lib-myFFmpeg.cpp
        JNICallbackHelper.cpp
        VideoChannel.cpp
        AudioChannel.cpp
        NativeSimplePlayer.cpp)

# 链接 FFmpeg 库
target_link_libraries(
        native-lib-myFFmpeg
        avcodec
        avformat
        avutil
        swscale
        swresample
        avfilter
        android
       ${log-lib}
)

set(CMAKE_BUILD_TYPE Debug)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -g")

