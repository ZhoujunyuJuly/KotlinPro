package com.example.coroutine.jetpack.room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coroutine.R;
import com.example.coroutine.databinding.ActivityRoomBinding;
import com.example.coroutine.jetpack.room.adapter.RoomAdapter;
import com.example.coroutine.jetpack.room.db.PersonBean;
import com.example.coroutine.jetpack.room.db.PersonDao;
import com.example.coroutine.jetpack.room.db.PersonDataBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoomActivity extends AppCompatActivity {

    private ActivityRoomBinding mBinding;

    private RoomAdapter mAdapter;
    private PersonDao mPersonDao;
    private ExecutorService mService;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mBinding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mService = Executors.newSingleThreadExecutor();
        mHandler = new Handler(Looper.getMainLooper());

        mAdapter = new RoomAdapter(this,new ArrayList<>());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerview.setAdapter(mAdapter);
        mPersonDao = PersonDataBase.getInstance(this).getPersonDao();
    }

    public void onAdd(View view) {
        mService.execute(() -> {
            mPersonDao.insert(PersonBean.getDatas(20).toArray(new PersonBean[0]));
            onCheck(mBinding.btnCheck);
        });
    }

    public void onDelete(View view) {
        mService.execute(() -> {
            List<PersonBean> allPerson = mPersonDao.getAllPerson();
            mPersonDao.delete(allPerson.subList(0,3).toArray(new PersonBean[0]));
            onCheck(mBinding.btnCheck);
        });
    }

    public void onModify(View view) {

        mService.execute(()->{
            List<PersonBean> allPerson = mPersonDao.getAllPerson();
            PersonBean bean = allPerson.get(0);
            bean.name = "更新了";
            bean.age = 100;
            mPersonDao.update(bean);
            onCheck(mBinding.btnCheck);
        });
    }

    public void onCheck(View view) {
        mService.execute(new Runnable() {
            @Override
            public void run() {
                List<PersonBean> list = mPersonDao.getAllPerson();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addDatas(list);
                    }
                });
            }
        });
    }

}