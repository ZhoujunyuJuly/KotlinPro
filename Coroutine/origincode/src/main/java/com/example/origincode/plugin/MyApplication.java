package com.example.origincode.plugin;

import android.app.Application;
import android.content.Context;

import com.example.origincode.plugin.Hook.HookUtil;
import com.example.origincode.plugin.Hook.HookUtil2;
import com.example.origincode.plugin.Hook.PluginApk;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            PluginApk.loadApk(this);
            HookUtil2.hookAMS();
            HookUtil2.hookHandler();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
