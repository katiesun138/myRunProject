package com.example.asn1

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Calendar
import java.util.Date

@Entity(tableName = "history_table")
data class HistoryData(
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0L,

    @ColumnInfo(name = "InputType")
    var inputType: Int = 0,

    @ColumnInfo(name = "ActivityType")
    var activityType: Int = 0,

    @ColumnInfo(name = "DateTime")
    var dateTime: Calendar,

    @ColumnInfo(name = "Duration")
    var duration: Double,

    @ColumnInfo(name = "Distance")
    var distance: Double,

    @ColumnInfo(name = "AvgPace")
    var avgPace: Double,

    @ColumnInfo(name = "AvgSpeed")
    var avgSpeed: Double,

    @ColumnInfo(name = "Calorie")
    var calorie: Double,

    @ColumnInfo(name = "HeartRate")
    var heartRate: Double,

    @ColumnInfo(name = "Comment")
    var comment: String,

//    @ColumnInfo(name = "LocationList")
//    var locationList: ArrayList<LatLng>

)
