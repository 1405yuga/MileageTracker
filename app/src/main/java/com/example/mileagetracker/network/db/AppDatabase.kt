package com.example.mileagetracker.network.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mileagetracker.data.JourneyData
import com.example.mileagetracker.data.PointsData
import com.example.mileagetracker.network.dao.JourneyDao
import com.example.mileagetracker.network.dao.PointsDao

@Database(entities = [JourneyData::class, PointsData::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journeyDao(): JourneyDao
    abstract fun pointsDao(): PointsDao
}