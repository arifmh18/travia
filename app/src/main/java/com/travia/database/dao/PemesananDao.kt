package com.travia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.travia.database.entity.PemesananEntity

@Dao
interface PemesananDao {
    @Query("SELECT * FROM pesanan")
    fun getALl(): LiveData<List<PemesananEntity>>
}