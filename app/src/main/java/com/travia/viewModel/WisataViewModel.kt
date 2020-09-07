package com.travia.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.travia.database.AppDatabase
import com.travia.database.entity.WisataEntity
import com.travia.repository.WisataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WisataViewModel() :ViewModel(){
    lateinit var repository: WisataRepository
    lateinit var allWisata: LiveData<List<WisataEntity>>

    fun init(context: Context){
        val wisataDao = AppDatabase.getDatabase(context).wisataDao()
        repository = WisataRepository(wisataDao)
        allWisata = repository.allWisata
    }

    fun insertAll(users: List<WisataEntity>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllWisata()
        repository.insertAllWisata(users)
    }
}