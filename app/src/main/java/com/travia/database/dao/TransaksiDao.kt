package com.travia.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.travia.database.entity.TransaksiEntity
import com.travia.database.entity.WisataEntity

@Dao
interface TransaksiDao {
    @Query("SELECT * FROM transaksi")
    fun getAll(): LiveData<List<TransaksiEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transaksi: List<TransaksiEntity>)

    @Query("DELETE FROM transaksi")
    suspend fun deleteAll()

    @Update(entity = TransaksiEntity::class)
    suspend fun updateTransaksi(transaksi: TransaksiEntity)

}