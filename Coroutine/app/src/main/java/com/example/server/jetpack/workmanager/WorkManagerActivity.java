package com.example.server.jetpack.workmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkContinuation;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkQuery;

import com.example.coroutine.R;
import com.example.server.jetpack.workmanager.works.WorkA;
import com.example.server.jetpack.workmanager.works.WorkB;
import com.example.server.jetpack.workmanager.works.WorkC;
import com.example.server.jetpack.workmanager.works.WorkD;
import com.example.server.jetpack.workmanager.works.WorkE;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class WorkManagerActivity extends AppCompatActivity {

    private WorkManager mWorkManager;
    private OneTimeWorkRequest request1;
    private OneTimeWorkRequest request2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_work_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addWork(View view) {
        mWorkManager = WorkManager.getInstance(this);

        //1.构造任务
        request1 = createRequest(MyWork.class);
        request2 = createRequest(HerWork.class);
        //周期任务
        PeriodicWorkRequest request3 = createPeriodWork(MyWork.class);

        //单任务
        //runOneTask();
        //链式任务
        //runMultiTask();
        //组合任务
        runGroupTask();

        //获取任务状态
        getWorkState();

        //取消任务
        //cancelTask();
    }


    /**
     * 任务执行条件
     * @return
     */
    private Constraints createConstraint(){
        return new Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build();
    }

    /**
     * 🌟一次性任务
     */
    private OneTimeWorkRequest createRequest(Class<? extends
            ListenableWorker> clz) {
        //发起任务时传递数据
        Data data = new Data.Builder()
                .putString("inputData", "Jack")
                .build();

        return new OneTimeWorkRequest
                .Builder(clz)
                /**
                 * 🍄设置触发条件
                 */
                .setConstraints(createConstraint())
                //延迟5s执行
                .setInitialDelay(5, TimeUnit.SECONDS)
                //指数退避策略,结果返回重试时线性重试
                .setBackoffCriteria(BackoffPolicy.LINEAR,
                        Duration.ofSeconds(2))
                //任务标签
                .addTag(clz.getName())
                //设置任务传入数据
                .setInputData(data)
                .build();
    }


    /**
     * 周期性任务
     * @return
     */
    private PeriodicWorkRequest createPeriodWork(Class<? extends
            ListenableWorker> clz){
        /**
         * 🌟周期性任务，最少15分钟间隔
         */
         return new PeriodicWorkRequest
                .Builder(clz, Duration.ofMinutes(15))
                .build();
    }

    /**
     * 🌟单任务执行
     */
    private void runOneTask(){
        mWorkManager.enqueue(request1);
    }

    /**
     * 链式任务
     */
    private void runMultiTask(){
        //打印监听，WorkInfo{mState=ENQUEUED, mOutputData=Data {}, mTags=[MyWork], mProgress=Data {}}
        //打印监听，WorkInfo{mState=BLOCKED, mOutputData=Data {}, mTags=[HerWork], mProgress=Data {}}
        //我的任务,inputData = Jack
        //打印监听，WorkInfo{mState=SUCCEEDED, mOutputData=Data {outputData : isOK, }, mTags=[MyWork], mProgress=Data {}}
        //输出数据==isOK
        //打印监听，WorkInfo{mState=ENQUEUED, mOutputData=Data {}, mTags=[HerWork], mProgress=Data {}}
        //HerWork: 第二个任务
        //打印监听，WorkInfo{mState=SUCCEEDED, mOutputData=Data {}, mTags=[HerWork], mProgress=Data {}}
        //输出数据==null
        mWorkManager
                .beginWith(request1)
                .then(request2)
                .enqueue();
    }

    private void runGroupTask(){
        WorkContinuation continuation1 = mWorkManager
                .beginWith(createRequest(WorkA.class))
                .then(createRequest(WorkB.class));

        WorkContinuation continuation2 = mWorkManager
                .beginWith(createRequest(WorkC.class))
                .then(createRequest(WorkD.class));

        List<WorkContinuation> list = new ArrayList<>();
        list.add(continuation1);
        list.add(continuation2);

        WorkContinuation
                /**
                 * 💜任务组合
                 */
                .combine(list)
                /**
                 * 然后再执行E
                 */
                .then(createRequest(WorkE.class))
                .enqueue();
    }


    /**
     * 🐒任务监听，观察任务状态
     */
    private void getWorkState(){
        List<UUID> ids = Arrays.asList(request1.getId(), request2.getId());
        mWorkManager.getWorkInfosLiveData(WorkQuery.fromIds(ids)).observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                for (WorkInfo workInfo: workInfos) {
//                    Log.d("zjy","打印监听，" + workInfo.toString());
                    if( workInfo.getState() == WorkInfo.State.SUCCEEDED){
                        /**
                         * 任务完成时，获取任务返回的数据
                         */
                        String outputData = workInfo.getOutputData().getString("outputData");
//                        Log.d("zjy", "输出数据 =" + outputData);
                        Log.d("zjy", workInfo.toString());
                    }
                }
            }
        });
    }


    private void cancelTask(){
        /**
         * 取消任务，其他任务进行时这段代码需要注释
         */
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mWorkManager.cancelWorkById(request1.getId());
            }
        },2000);
    }
}

