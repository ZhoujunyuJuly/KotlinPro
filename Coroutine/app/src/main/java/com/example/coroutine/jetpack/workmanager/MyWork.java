package com.example.coroutine.jetpack.workmanager;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWork extends Worker {

    public MyWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SystemClock.sleep(2000);
        String inputData = getInputData().getString("inputData");

        Log.d("zjy","我的任务,inputData = " + inputData);

        //返回任务数据
        Data data = new Data.Builder().putString("outputData", "isOK").build();
        return Result.success(data);
    }
}
