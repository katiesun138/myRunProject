package com.example.asn1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Insert
    suspend fun insertLocationPoint(locationPoint: LocationPoint)

    @Query("SELECT * FROM location_points")
    suspend fun getAllLocationPoints(): List<LocationPoint>
}