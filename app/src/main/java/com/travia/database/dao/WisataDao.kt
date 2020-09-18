package com.travia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.travia.database.entity.WisataEntity

@Dao
interface WisataDao{

    @Query("SELECT * FROM wisata")
    fun getAll():LiveData<List<WisataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(wisata: List<WisataEntity>)

    @Query("DELETE FROM wisata")
    suspend fun deleteAll()


    @Query("SELECT * FROM wisata WHERE nama LIKE :query AND kategory = :kategori")
    fun getPencarian (query:String, kategori:String):LiveData<List<WisataEntity>>
}