package com.example.server.mvvm.mvvm;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.coroutine.R;
import com.example.coroutine.databinding.ActivityMvvmBinding;

public class MVVMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mvvm);
        ActivityMvvmBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_mvvm);
        ContentViewModel viewModel = new ViewModelProvider(this).get(ContentViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

    }
}
