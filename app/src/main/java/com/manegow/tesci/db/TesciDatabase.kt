package com.manegow.tesci.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [User::class, Semester::class, Rating::class],
    version = 1,
    exportSchema = false
)
abstract class TesciDatabase : RoomDatabase() {

    abstract fun TesciDao(): TesciDao

    companion object {
        @Volatile
        private var INSTANCE: TesciDatabase? = null

        fun getInstance(context: Context): TesciDatabase {
            synchronized(TesciDatabase::class.java) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            TesciDatabase::class.java,
                            "tesci_database"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
