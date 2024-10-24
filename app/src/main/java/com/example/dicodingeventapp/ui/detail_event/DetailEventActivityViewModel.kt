package com.example.dicodingeventapp.ui.detail_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailEventActivityViewModel(private val eventRepository: EventRepository) :
    ViewModel() {

//    private val _event = MutableLiveData<Event>()
//    val event: LiveData<Event> = _event


//    not using this function we will getting all the information from the intent
//    val detailEvent = eventRepository.fetchDetailEvent(eventId)


    suspend fun deleteEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.setFavoriteEvents(event, false)
        }
    }

    suspend fun saveEvent(event: Event) {
        viewModelScope.launch(Dispatchers.IO) {
            eventRepository.setFavoriteEvents(event, true)
        }

    }

}