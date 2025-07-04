package com.example.coroutine.dataBinding.net

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    var userViewModel = MutableLiveData<User>()

    fun getUser() {
        viewModelScope.launch {
            userViewModel.value = provideUserServiceApi().getUser()
        }
    }
}