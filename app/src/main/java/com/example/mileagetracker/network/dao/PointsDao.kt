package com.example.mileagetracker.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mileagetracker.data.PointsData

@Dao
interface PointsDao {
    companion object {
        val mock = object : PointsDao {
            override suspend fun insertPointData(point: PointsData): Long = 0L

            override suspend fun getPointsFromJourney(journeyId: Long): List<PointsData> =
                emptyList()

        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPointData(point: PointsData): Long

    @Query("SELECT * FROM ${PointsData.TABLE_NAME} WHERE ${PointsData.COLUMN_JOURNEY_ID} = :journeyId")
    suspend fun getPointsFromJourney(journeyId: Long): List<PointsData>
}