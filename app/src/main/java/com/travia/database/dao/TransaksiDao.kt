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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaksi: TransaksiEntity)

    @Query("DELETE FROM transaksi")
    suspend fun deleteAll()

    @Query("DELETE FROM transaksi WHERE `key` = :query")
    suspend fun delete(query: String)

    @Update(entity = TransaksiEntity::class)
    suspend fun updateTransaksi(transaksi: TransaksiEntity)

}