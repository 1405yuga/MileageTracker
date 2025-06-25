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
    suspend fun deleteJourneyById(id: Long): Int

}