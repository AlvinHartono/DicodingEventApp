package com.example.dicodingeventapp.ui.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.Result
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableLiveData<Result<List<Event>>>()
    val upcomingEvents: LiveData<Result<List<Event>>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    suspend fun fetchUpcomingEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _upcomingEvents.postValue(Result.Loading)
            _isLoading.postValue(true)

            try {
                Log.d("UpcomingViewModel", "Fetching upcoming events...")
                val response = eventRepository.fetchUpcomingEvents()
                Log.d("UpcomingViewModel", "Response 1: ${response.value}")
                when (response.value) {
                    is Result.Success -> {
                        _upcomingEvents.postValue(response.value)
                        Log.d("UpcomingViewModel", "Upcoming events fetched successfully")
                        _isLoading.postValue(false)
                    }

                    is Result.Loading -> {
                        _upcomingEvents.postValue(Result.Loading)
                        Log.d("UpcomingViewModel", "Upcoming events are still loading")
                    }

                    else -> {
                        _upcomingEvents.postValue(Result.Error("Failed to fetch upcoming events: ${response.value}"))
                        Log.e("UpcomingViewModel", "Failed to fetch upcoming events: ${response.value}")
                        _isLoading.postValue(false)
                    }
                }
            } catch (e: Exception) {
                _upcomingEvents.postValue(Result.Error("error dari UpcomingViewModel " + e.message.toString()))
                _isLoading.postValue(false)
            }
        }
    }


    fun saveEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.setFavoriteEvents(event, true)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.setFavoriteEvents(event, false)
        }
    }
}