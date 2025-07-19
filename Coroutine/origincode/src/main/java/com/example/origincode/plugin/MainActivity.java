package com.example.origincode.plugin;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.origincode.R;

import java.io.File;
import java.lang.reflect.Field;

import com.example.origincode.plugin.Hook.HookUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    public void jumpToPlugin(View view) {
        //startActivity(new Intent(this, MainActivity.class));

//        Intent proxyIntent = new Intent();

//        proxyIntent.putExtra("target_intent",originIntent);
//        proxyIntent.setClassName("com.example.origincode",
//                "com.example.origincode.plugin.ProxyNewActivity");
//        startActivity(proxyIntent);
        Intent pluginIntent = new Intent();
        pluginIntent.setComponent(new ComponentName("com.example.plugin","com.example.plugin.MainActivity"));
        startActivity(pluginIntent);

    }

    private static void printAMSInstances() throws Exception {
        Class<?> activityManagerClz = Class.forName("android.app.ActivityManager");
        Field singletonField = activityManagerClz.getDeclaredField("IActivityManagerSingleton");
        singletonField.setAccessible(true);
        Object amSingleton = singletonField.get(null);
        Field instanceField = Class.forName("android.util.Singleton").getDeclaredField("mInstance");
        instanceField.setAccessible(true);
        Object amInstance = instanceField.get(amSingleton);
        Log.d("zjya","IActivityManagerSingleton mInstance:" + amInstance);

        try {
            Class<?> activityTaskManagerClz = Class.forName("android.app.ActivityTaskManager");
            Field taskSingletonField = activityTaskManagerClz.getDeclaredField("IActivityTaskManagerSingleton");
            taskSingletonField.setAccessible(true);
            Object taskSingleton = taskSingletonField.get(null);
            Object taskInstance = instanceField.get(taskSingleton);
            Log.d("zjya","zjy,IActivityTaskManagerSingleton mInstance: " + taskInstance);
        } catch (NoSuchFieldException e) {
            Log.d("zjya","zjy,No IActivityTaskManagerSingleton found ," + e);
        }
    }

}