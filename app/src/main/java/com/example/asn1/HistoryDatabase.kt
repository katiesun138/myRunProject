package com.example.asn1

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [HistoryData::class], version = 1)
@TypeConverters(CalendarTypeConverter::class)
abstract class HistoryDatabase:RoomDatabase() {
    abstract val historyDataDao: HistoryDataDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, HistoryDatabase::class.java,"kt_db").build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}