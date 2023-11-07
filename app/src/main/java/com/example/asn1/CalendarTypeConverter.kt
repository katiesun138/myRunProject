package com.example.asn1

import androidx.room.TypeConverter
import java.util.Calendar

class CalendarTypeConverter {
    @TypeConverter
    fun fromCalendar(calendar: Calendar): Long {
        return calendar.timeInMillis
    }

    @TypeConverter
    fun toCalendar(timeInMillis: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return calendar
    }
}
