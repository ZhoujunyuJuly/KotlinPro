package com.example.coroutine.jetpack.room;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coroutine.R;
import com.example.coroutine.databinding.ActivityRoomBinding;
import com.example.coroutine.databinding.ActivityRoomOptimizeBinding;
import com.example.coroutine.jetpack.room.adapter.RoomAdapter;
import com.example.coroutine.jetpack.room.db.PersonBean;
import com.example.coroutine.jetpack.room.repository.PersonViewModel;

import java.util.ArrayList;
import java.util.List;

public class RoomOptimizeActivity extends AppCompatActivity {
    private ActivityRoomOptimizeBinding mBinding;
    private RoomAdapter mAdapter;
    private PersonViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mBinding = ActivityRoomOptimizeBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAdapter = new RoomAdapter(this,new ArrayList<>());
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerview.setAdapter(mAdapter);


        mViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        mViewModel.getAllData().observe(this, new Observer<List<PersonBean>>() {
            @Override
            public void onChanged(List<PersonBean> beans) {
                mAdapter.addDatas(beans);
            }
        });
       mViewModel.add(PersonBean.getDatas(5).toArray(new PersonBean[0]));
    }

    public void onAdd(View view) {
        PersonBean bean = new PersonBean("Rose",17);
        mViewModel.add(bean);
    }

    public void onDelete(View view) {
        mViewModel.delete();
    }

    public void onModify(View view) {
        mViewModel.modify();
    }

    public void onDeleteAll(View view) {
        mViewModel.clearAll();
    }
}