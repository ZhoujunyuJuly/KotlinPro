package com.example.coroutine.exPro.net.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CakeEntity(
    @PrimaryKey val name:String,val icon:String,val page:Int)