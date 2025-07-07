package com.example.coroutine.exPro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coroutine.exPro.net.model.CakeItemModel
import com.example.coroutine.exPro.repository.CakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CakeViewModel @Inject constructor (val repository: CakeRepository) : ViewModel() {

    val data : LiveData<PagingData<CakeItemModel>> =
        repository.getCakeList().
        cachedIn(viewModelScope).
        asLiveData()
}