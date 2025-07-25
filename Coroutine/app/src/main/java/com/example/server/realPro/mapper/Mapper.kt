package com.example.server.realPro.mapper

import com.example.server.realPro.model.CarBrandEntity
import com.example.server.realPro.model.CarBrandItemModel

interface Mapper {
    fun map(entity: CarBrandEntity):CarBrandItemModel
}