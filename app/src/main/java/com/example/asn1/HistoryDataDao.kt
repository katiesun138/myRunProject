package com.example.asn1

import android.text.InputType
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.w3c.dom.Comment
import java.time.Duration
import java.util.Calendar
import java.util.Date

@Dao
interface HistoryDataDao {
    @Insert
    suspend fun insertActivity(historyData:HistoryData)

    @Query("SELECT * FROM history_table")
    fun getAllHistoryDisplay(): Flow<List<HistoryData>>
    @Query("DELETE FROM history_table")
    suspend fun deleteAll()

    @Query("DELETE FROM history_table WHERE id = :key")
    suspend fun deleteEntry(key:Long)
}