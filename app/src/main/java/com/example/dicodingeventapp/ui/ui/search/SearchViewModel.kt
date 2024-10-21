package com.example.dicodingeventapp.ui.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository

class SearchViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _searchedEvents = MutableLiveData<List<Event?>>()
    val searchedEvents: LiveData<List<Event?>> = _searchedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchSearchResult(query: String) = eventRepository.fetchSearchResult(query)

}