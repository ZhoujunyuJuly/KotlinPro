package com.example.server.jetpack.workmanager.works;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkD extends Worker {

    public WorkD(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("zjy", "进入doWork()->" + WorkD.class.getName());
        return Result.success();
    }
}
