package com.example.mileagetracker.network.repositoy

import com.example.mileagetracker.data.JourneyData
import com.example.mileagetracker.network.dao.JourneyDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JourneyRepository @Inject constructor(private val journeyDao: JourneyDao) {
    companion object {
        val mock: JourneyRepository = JourneyRepository(JourneyDao.mock)
    }

    suspend fun insertJourney(
        journey: JourneyData,
        onSuccess: (id: Long) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        try {
            val id = journeyDao.insertJourney(journey = journey)
            onSuccess(id)
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure(e)
        }
    }

    suspend fun getAllJourneys(): List<JourneyData> {
        return journeyDao.getAllJourneys()
    }

    suspend fun deleteJourneyById(
        id: Long,
        onSuccess: (id: Long) -> Unit,
        onFailure: (Throwable) -> Unit
    ) {
        try {
            journeyDao.deleteJourneyById(id = id)
            onSuccess(id)
        } catch (e: Exception) {
            e.printStackTrace()
            onFailure(e)
        }
    }

    suspend fun updateEndTime(journeyId: Long, endTime: Long) {
        journeyDao.updateEndTime(journeyId, endTime)
    }
}