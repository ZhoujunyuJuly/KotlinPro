package com.example.coroutine.jetpack.databinding;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.coroutine.R;
import com.example.coroutine.databinding.ActivityDatabindingBinding;
import com.example.coroutine.jetpack.databinding.model.field.UserViewModelField;
import com.example.coroutine.jetpack.databinding.model.observable.UseViewModel;
import com.example.coroutine.jetpack.databinding.model.solution.UserModelSolution;
import com.example.coroutine.realPro.model.CommonConstant;

public class DataBindingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_databinding);
        ActivityDatabindingBinding viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_databinding);
        viewBinding.setNetWorkImage(CommonConstant.IMAGE_URL);
        //viewBinding.setDefaultImage(R.mipmap.ic_launcher);
//        viewBinding.setUserViewModel(new UserViewModelField());
        viewBinding.setUserViewModel(new UserModelSolution());
        viewBinding.setLifecycleOwner(this);
    }
}