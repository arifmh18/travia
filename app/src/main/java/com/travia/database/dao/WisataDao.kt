package com.travia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.travia.WisataModel

@Dao
interface WisataDao{

    @Query("SELECT * FROM wisata")
    fun getAll():LiveData<List<WisataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coronas: List<WisataModel>)

    @Query("DELETE FROM wisata")
    suspend fun deleteAll()
}