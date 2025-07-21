package com.example.video;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

public class VideoActivity extends AppCompatActivity {

    private SimplePlayer mPlayer;
    public static final String TAG = VideoActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView tv = findViewById(R.id.video_text);

        //tv.setText(mPlayer.getFFmpegVersion());
        initPlayer();
    }


    private void initPlayer(){
        mPlayer = new SimplePlayer();
        File videFile = new File(Environment.getExternalStorageDirectory()
                + File.separator + "Download/Mcloud.mp4");
        mPlayer.setDataSource(videFile.getAbsolutePath());
        //准备成功的回调——C++子线程调用
        mPlayer.setOnPreparedListener(new SimplePlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                Log.d(TAG, "onPrepared: 准备完成");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VideoActivity.this,"准备完成，即将播放",Toast.LENGTH_SHORT).show();
                    }
                });
                mPlayer.start();
            }
        });

        mPlayer.setOnErrorListener(new SimplePlayer.OnErrorListener() {
            @Override
            public void onError(String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(VideoActivity.this,"异常出错",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //触发
        mPlayer.prepare();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }
}