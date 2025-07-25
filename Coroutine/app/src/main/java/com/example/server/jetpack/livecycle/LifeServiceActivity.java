package com.example.server.jetpack.livecycle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.coroutine.R;

public class LifeServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_life_service);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startGps();
        Log.d("zjy","开启服务");
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopGps();
        Log.d("zjy","关闭服务");
    }

    public void startGps(){
        startService(new Intent(this,MyLocationService.class));
    }

    public void stopGps(){
        stopService(new Intent(this,MyLocationService.class));
    }
}