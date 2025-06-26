package com.example.mileagetracker.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mileagetracker.data.Summary
import com.example.mileagetracker.network.repositoy.JourneyRepository
import com.example.mileagetracker.network.repositoy.PointsRepository
import com.example.mileagetracker.utils.helper.HelperFunctions
import com.example.mileagetracker.utils.screen_state.ScreenState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val journeyRepository: JourneyRepository, private val pointsRepository: PointsRepository
) : ViewModel() {
    private val _summaryList = MutableStateFlow<List<Summary>>(emptyList())
    val summaryList: StateFlow<List<Summary>> = _summaryList

    private val _screenState = MutableStateFlow<ScreenState<List<Summary>>>(ScreenState.PreLoad())
    val screenState: StateFlow<ScreenState<List<Summary>>> = _screenState

    init {
        loadAllSummary()
    }

    fun loadAllSummary() {
        _screenState.value = ScreenState.Loading()
        viewModelScope.launch {
            try {
                val result = coroutineScope {
                    val journeyList = journeyRepository.getAllJourneys()
                    val deferredSummaries = journeyList.map { journeyData ->
                        async {
                            val points =
                                pointsRepository.getPointsFromJourney(journeyId = journeyData.id)
                                    .map { LatLng(it.latitude, it.longitude) }
                            Summary(
                                id = journeyData.id,
                                title = journeyData.title,
                                points = points,
                                distanceInMeters = HelperFunctions.calculateDistance(pointsList = points),
                                startTime = journeyData.startTime,
                                endTime = journeyData.endTime
                            )
                        }
                    }

                    deferredSummaries.awaitAll()
                }
                _summaryList.value = result
                _screenState.value = ScreenState.Loaded(result = result)
            } catch (e: Exception) {
                e.printStackTrace()
                _screenState.value = ScreenState.Error("Unable to load")
            }
        }
    }
}