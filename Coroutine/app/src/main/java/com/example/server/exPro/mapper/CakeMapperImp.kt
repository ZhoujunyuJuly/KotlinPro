package com.example.server.exPro.mapper

import com.example.server.exPro.net.model.CakeEntity
import com.example.server.exPro.net.model.CakeItemModel
import javax.inject.Inject

class CakeMapperImp @Inject constructor() : CakeMapper<CakeEntity,CakeItemModel> {
    override fun convert(item: CakeEntity): CakeItemModel {
       return CakeItemModel(item.name,item.icon)
    }
}