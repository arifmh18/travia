package com.travia.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.travia.database.AppDatabase
import com.travia.database.entity.PemesananEntity
import com.travia.repository.PemesananRepository

class PemesananViewModel() : ViewModel() {

    private lateinit var repo: PemesananRepository
    private lateinit var allPemesanan: LiveData<List<PemesananEntity>>

    fun init(context: Context) {
        val dao = AppDatabase.getDatabase(context).pemesananDao()
        repo = PemesananRepository(dao)
        allPemesanan = repo.allPemesanan
    }

}