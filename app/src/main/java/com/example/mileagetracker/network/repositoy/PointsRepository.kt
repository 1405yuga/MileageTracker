package com.example.mileagetracker.network.repositoy

import com.example.mileagetracker.data.PointsData
import com.example.mileagetracker.network.dao.PointsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PointsRepository @Inject constructor(private val pointsDao: PointsDao) {
    suspend fun insertPointData(point: PointsData): Long {
        return pointsDao.insertPointData(point = point)
    }

    fun getPointsFromJourney(journeyId: Long): Flow<List<PointsData>> {
        return pointsDao.getPointsFromJourney(journeyId = journeyId)
    }
}