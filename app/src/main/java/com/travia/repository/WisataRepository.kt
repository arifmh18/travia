package com.travia.repository

import androidx.lifecycle.LiveData
import com.travia.WisataModel
import com.travia.database.dao.WisataDao

class WisataRepository(private val wisataDao: WisataDao) {
    val allWisata: LiveData<List<WisataModel>> = wisataDao.getAll()

    suspend fun insertAllWisata(wisata: List<WisataModel>) {
        wisataDao.insertAll(wisata)
    }

    suspend fun deleteAllWisata() {
        wisataDao.deleteAll()
    }
}