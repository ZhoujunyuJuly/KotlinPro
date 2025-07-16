package com.example.coroutine.jetpack.workmanager.works;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WorkB extends Worker {

    public WorkB(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("zjy", "进入doWork()->" + WorkB.class.getName());
        return Result.success();
    }
}
