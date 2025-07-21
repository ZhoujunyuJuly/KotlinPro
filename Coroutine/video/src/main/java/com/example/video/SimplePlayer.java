package com.example.video;


public class SimplePlayer {

    public native String getFFmpegVersion();

    static {
        System.loadLibrary("native-lib-myFFmpeg");
    }


    public SimplePlayer() {

    }


    private String dataSource;

    private OnPreparedListener onPreparedListener;
    private OnErrorListener onErrorListener;

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 播放器的准备工作
     */
    public void prepare(){
        prepareNative(dataSource);
    }

    /**
     * 开始播放
     */
    public void start(){
        startNative();
    }

    /**
     * 停止播放
     */
    public void stop(){
        stopNative();
    }

    /**
     * 释放资源
     */
    public void release(){
        releaseNative();
    }


    public void onPrepared(){
        if( onPreparedListener != null){
            onPreparedListener.onPrepared();
        }
    }

    public void onError(int errorCode){
        onErrorListener.onError("有异常");
    }


    public void setOnPreparedListener(OnPreparedListener listener){
        onPreparedListener = listener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    public interface OnPreparedListener{
        void onPrepared();
    }

    public interface OnErrorListener{
        void onError(String msg);
    }

    /**
     * ======================= native 函数区 ==========================
     */

    private native void prepareNative(String dataSource);
    private native void startNative();
    private native void stopNative() ;
    private native void releaseNative() ;

}
