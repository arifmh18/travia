package com.travia.repository

import androidx.lifecycle.LiveData
import com.travia.database.dao.PemesananDao
import com.travia.database.entity.PemesananEntity

class PemesananRepository(private val dao: PemesananDao) {

    val allPemesanan: LiveData<List<PemesananEntity>> = dao.getALl()
}