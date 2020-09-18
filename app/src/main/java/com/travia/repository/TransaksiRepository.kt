package com.travia.repository

import androidx.lifecycle.LiveData
import com.travia.database.dao.TransaksiDao
import com.travia.database.entity.TransaksiEntity

class TransaksiRepository(private val dao: TransaksiDao) {
    val allTransaksi: LiveData<List<TransaksiEntity>> = dao.getAll()

    suspend fun insertAll(transaksi: List<TransaksiEntity>){
        dao.insertAll(transaksi)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    suspend fun update(transaksiEntity: TransaksiEntity) {
        dao.updateTransaksi(transaksiEntity)
    }

    suspend fun insert(transaksiEntity: TransaksiEntity) {
        dao.insert(transaksiEntity)
    }

    suspend fun delete(key: String) {
        dao.delete(key)
    }
}