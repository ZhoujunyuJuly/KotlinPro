package com.example.coroutine.exPro.mapper

import com.example.coroutine.exPro.net.model.CakeEntity
import com.example.coroutine.exPro.net.model.CakeItemModel
import javax.inject.Inject

class CakeMapperImp @Inject constructor() : CakeMapper<CakeEntity,CakeItemModel> {
    override fun convert(item: CakeEntity): CakeItemModel {
       return CakeItemModel(item.name,item.icon)
    }
}