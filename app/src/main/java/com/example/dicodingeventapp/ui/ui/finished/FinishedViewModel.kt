package com.example.dicodingeventapp.ui.ui.finished

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


class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private var _finishedEvents = MutableLiveData<Result<List<Event>>>()
    val finishedEvents: LiveData<Result<List<Event>>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun fetchFinishedEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            _finishedEvents.postValue(Result.Loading)
            _isLoading.postValue(true)

            try {
                val response = eventRepository.fetchFinishedEvents()
                when (response.value) {
                    is Result.Success -> {
                        _finishedEvents.postValue(response.value)
                        _isLoading.postValue(false)
                        Log.d("FinishedViewModel", "Finished events fetched successfully")
                    }

                    is Result.Loading -> {
                        _finishedEvents.postValue(Result.Loading)
                        Log.d("FinishedViewModel", "Finished events are still loading")
                    }

                    else -> {
                        _finishedEvents.postValue(Result.Error("Failed to fetch finished events: ${response.value}"))
                        _isLoading.postValue(false)
                        Log.e("FinishedViewModel", "Failed to fetch finished events: ${response.value}")
                    }
                }

            } catch (e: Exception) {
                _finishedEvents.postValue(Result.Error(e.message.toString()))
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