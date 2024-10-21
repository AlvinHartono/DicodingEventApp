package com.example.dicodingeventapp.ui.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {

    // i don't think this will be used
    private val _upcomingEvents = MutableLiveData<List<Event?>>()
    val upcomingEvents: LiveData<List<Event?>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchUpcomingEvents() = eventRepository.fetchUpcomingEvents()

    fun saveEvent(event: Event) {
        eventRepository.setFavoriteEvents(event, true)
    }

    fun deleteEvent(event: Event) {
        eventRepository.setFavoriteEvents(event, false)

    }
}