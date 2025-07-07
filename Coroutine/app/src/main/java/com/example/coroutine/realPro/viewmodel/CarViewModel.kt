package com.example.coroutine.realPro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.coroutine.realPro.model.CarBrandItemModel
import com.example.coroutine.realPro.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor (
    repository: Repository
):ViewModel() {
    val data : LiveData<PagingData<CarBrandItemModel>> = repository
        .fetchCarBrandList()
        .cachedIn(viewModelScope)
        .asLiveData()
}