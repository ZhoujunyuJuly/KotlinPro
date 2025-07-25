package com.example.server.realPro.mapper

import com.example.server.realPro.model.CarBrandEntity
import com.example.server.realPro.model.CarBrandItemModel
import javax.inject.Inject

class CarEntity2ItemModel @Inject constructor()  : Mapper {
    override fun map(entity: CarBrandEntity)
    : CarBrandItemModel = CarBrandItemModel(
        id = entity.id,
        name = entity.name,
        icon = entity.icon)
}