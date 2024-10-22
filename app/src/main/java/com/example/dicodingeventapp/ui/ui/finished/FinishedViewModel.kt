package com.example.dicodingeventapp.ui.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository


class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {

    // i don't think this will be used
//    private var _finishedEvents = MutableLiveData<List<Event?>>()
//    val finishedEvents: LiveData<List<Event?>> = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchFinishedEvents() = eventRepository.fetchFinishedEvents()

    fun saveEvent(event: Event) = eventRepository.setFavoriteEvents(event, true)

    fun deleteEvent(event: Event) = eventRepository.setFavoriteEvents(event, false)
}