package com.example.server.realPro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.server.realPro.model.CarBrandItemModel
import com.example.server.realPro.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * HiltViewModel 关键字
 * 作用是让 ViewModel 可以使用 @Inject constructor
 */
@HiltViewModel
class CarViewModel @Inject constructor (
    repository: Repository
):ViewModel() {
    val data : LiveData<PagingData<CarBrandItemModel>> = repository
        .fetchCarBrandList()
        .cachedIn(viewModelScope)
        .asLiveData()
}