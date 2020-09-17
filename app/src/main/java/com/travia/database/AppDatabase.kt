package com.travia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.travia.database.dao.TransaksiDao
import com.travia.database.dao.WisataDao
import com.travia.database.entity.TransaksiEntity
import com.travia.database.entity.WisataEntity

@Database(
    entities = [WisataEntity::class, TransaksiEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase:RoomDatabase() {

    abstract fun wisataDao(): WisataDao
    abstract fun transaksiDao(): TransaksiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val temInstance = INSTANCE
            if (temInstance != null) {
                return temInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "travina_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}