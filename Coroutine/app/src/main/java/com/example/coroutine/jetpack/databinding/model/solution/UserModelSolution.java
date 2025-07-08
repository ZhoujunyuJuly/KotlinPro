package com.example.coroutine.jetpack.databinding.model.solution;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserModelSolution extends ViewModel {
    public final MutableLiveData<String> userName = new MutableLiveData<>();

}
