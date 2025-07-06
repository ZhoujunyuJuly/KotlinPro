package com.example.coroutine.realPro.mapper

import com.example.coroutine.realPro.model.CarBrandEntity
import com.example.coroutine.realPro.model.CarBrandItemModel

interface Mapper {
    fun map(entity: CarBrandEntity):CarBrandItemModel
}