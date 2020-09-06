package com.travia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.travia.WisataModel
import com.travia.database.dao.WisataDao

@Database(entities = [WisataModel::class],version = 5,exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun wisataDao():WisataDao

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
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}