package com.example.server.realPro.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.server.realPro.model.CarBrandEntity

@Dao
interface CarBrandDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarBrand(list:List<CarBrandEntity>)

    @Query("SELECT * FROM CarBrandEntity")
    fun getCarBrand():PagingSource<Int,CarBrandEntity>

    @Query("DELETE FROM CarBrandEntity")
    suspend fun clear()
}