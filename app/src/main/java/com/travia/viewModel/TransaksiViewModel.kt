package com.travia.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.travia.database.AppDatabase
import com.travia.database.entity.TransaksiEntity
import com.travia.repository.TransaksiRepository

class TransaksiViewModel() : ViewModel() {

    lateinit var repository: TransaksiRepository
    lateinit var allTransaksi: LiveData<List<TransaksiEntity>>

    fun init(context: Context) {
        val dao = AppDatabase.getDatabase(context).transaksiDao()
        repository = TransaksiRepository(dao)
        allTransaksi = repository.allTransaksi
    }
}