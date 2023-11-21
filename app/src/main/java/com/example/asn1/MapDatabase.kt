package com.example.asn1

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationPoint::class], version = 1, exportSchema = false)
abstract class LocationDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
