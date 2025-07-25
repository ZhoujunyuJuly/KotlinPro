package com.example.server.exPro.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.server.exPro.net.model.CakeEntity

@Dao
interface CakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCakeList(list:List<CakeEntity>)

    @Query("SELECT * FROM CakeEntity")
    fun getCake():PagingSource<Int,CakeEntity>

    @Query("DELETE FROM CakeEntity")
    fun clear()
}