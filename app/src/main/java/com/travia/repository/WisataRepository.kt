package com.travia.repository

import androidx.lifecycle.LiveData
import com.travia.database.dao.WisataDao
import com.travia.database.entity.WisataEntity

class WisataRepository(private val wisataDao: WisataDao) {
    val allWisata: LiveData<List<WisataEntity>> = wisataDao.getAll()

    suspend fun insertAllWisata(wisata: List<WisataEntity>) {
        wisataDao.insertAll(wisata)
    }

    suspend fun deleteAllWisata() {
        wisataDao.deleteAll()
    }

    fun search(sql: String, ktgr:String): LiveData<List<WisataEntity>> {
        return wisataDao.getPencarian(sql,ktgr)
    }
}