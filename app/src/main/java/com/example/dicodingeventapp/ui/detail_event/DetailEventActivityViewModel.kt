package com.example.dicodingeventapp.ui.detail_event

import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository

class DetailEventActivityViewModel(private val eventRepository: EventRepository) :
    ViewModel() {

//    private val _event = MutableLiveData<Event>()
//    val event: LiveData<Event> = _event


//    not using this function we will getting all the information from the intent
//    val detailEvent = eventRepository.fetchDetailEvent(eventId)


    fun deleteEvent(event: Event) = eventRepository.setFavoriteEvents(event, false)

    fun saveEvent(event: Event) = eventRepository.setFavoriteEvents(event, true)

}