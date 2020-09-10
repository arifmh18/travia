package com.travia.repository

import androidx.lifecycle.LiveData
import com.travia.database.dao.TransaksiDao
import com.travia.database.entity.TransaksiEntity

class TransaksiRepository(private val dao: TransaksiDao) {
    val allTransaksi: LiveData<List<TransaksiEntity>> = dao.getAll()

}