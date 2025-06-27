package com.example.mileagetracker.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mileagetracker.data.JourneyData

@Dao
interface JourneyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJourney(journey: JourneyData): Long

    @Query("SELECT * FROM ${JourneyData.TABLE_NAME}")
    suspend fun getAllJourneys(): List<JourneyData>

    @Query("DELETE FROM ${JourneyData.TABLE_NAME} WHERE id = :id")
    suspend fun deleteJourneyById(id: Long)

    @Query("UPDATE ${JourneyData.TABLE_NAME} SET ${JourneyData.COLUMN_END_TIME} = :endTime WHERE id = :id")
    suspend fun updateEndTime(id: Long, endTime: Long)

    companion object {
        val mock: JourneyDao = object : JourneyDao {

            override suspend fun insertJourney(journey: JourneyData): Long = 0L

            override suspend fun getAllJourneys(): List<JourneyData> = emptyList()

            override suspend fun deleteJourneyById(id: Long) {}

            override suspend fun updateEndTime(id: Long, endTime: Long) {}
        }
    }
}