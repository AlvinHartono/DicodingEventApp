package com.example.dicodingeventapp.ui.ui.favorite

import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.repository.EventRepository

class FavoriteViewModel(private val eventRepository: EventRepository) : ViewModel()  {

    fun getFavoriteEvents() = eventRepository.getFavoriteEvents()
}