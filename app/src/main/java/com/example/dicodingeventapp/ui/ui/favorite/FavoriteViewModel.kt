package com.example.dicodingeventapp.ui.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel()  {

    fun getFavoriteEvents() = eventRepository.getFavoriteEvents()

    fun saveEvent(event: Event) = eventRepository.setFavoriteEvents(event, true)

    fun deleteEvent(event: Event) = eventRepository.setFavoriteEvents(event, false)
}