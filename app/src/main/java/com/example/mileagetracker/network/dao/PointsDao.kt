package com.example.mileagetracker.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mileagetracker.data.PointsData
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointData(point: PointsData): Long

    @Query("SELECT * FROM ${PointsData.TABLE_NAME} WHERE ${PointsData.COLUMN_JOURNEY_ID} = :journeyId")
    fun getPointsFromJourney(journeyId: Long): Flow<List<PointsData>>
}