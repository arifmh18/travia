package com.travia.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travia.database.AppDatabase
import com.travia.database.entity.TransaksiEntity
import com.travia.repository.TransaksiRepository
import kotlinx.coroutines.launch

class TransaksiViewModel() : ViewModel() {

    lateinit var repository: TransaksiRepository
    lateinit var allTransaksi: LiveData<List<TransaksiEntity>>

    fun init(context: Context) {
        val dao = AppDatabase.getDatabase(context).transaksiDao()
        repository = TransaksiRepository(dao)
        allTransaksi = repository.allTransaksi
    }

    fun insertAll(transaksi: List<TransaksiEntity>) = viewModelScope.launch {
        repository.deleteAll()
        repository.insertAll(transaksi)
    }
}