package com.example.mileagetracker.network.repositoy

import com.example.mileagetracker.data.JourneyData
import com.example.mileagetracker.network.dao.JourneyDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JourneyRepository @Inject constructor(private val journeyDao: JourneyDao) {
    suspend fun insertJourney(journey: JourneyData): Long {
        return journeyDao.insertJourney(journey = journey)
    }

    suspend fun getAllJourneys(): List<JourneyData> {
        return journeyDao.getAllJourneys()
    }

    suspend fun deleteJourneyById(id: Long): Int {
        return journeyDao.deleteJourneyById(id = id)
    }
}