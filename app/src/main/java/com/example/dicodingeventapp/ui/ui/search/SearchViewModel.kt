package com.example.dicodingeventapp.ui.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingeventapp.data.Result
import com.example.dicodingeventapp.data.local.entity.Event
import com.example.dicodingeventapp.repository.EventRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _searchedEvents = MutableLiveData<Result<List<Event>>>()
    val searchedEvents: LiveData<Result<List<Event>>> = _searchedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchSearchResult(query: String) {
        _searchedEvents.postValue(Result.Loading)
        _isLoading.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = eventRepository.fetchSearchResult(query)
                _searchedEvents.postValue(response.value)
                _isLoading.postValue(false)
            } catch (e: Exception) {
                _searchedEvents.postValue(Result.Error(e.message.toString()))
                _isLoading.postValue(false)
            }
        }
    }
}