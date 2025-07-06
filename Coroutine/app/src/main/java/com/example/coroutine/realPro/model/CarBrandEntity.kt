package com.example.coroutine.realPro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CarBrandEntity(
    @PrimaryKey
    val id:Int,
    val name:String,
    val page:Int = 0,
    val icon:String
) {

}