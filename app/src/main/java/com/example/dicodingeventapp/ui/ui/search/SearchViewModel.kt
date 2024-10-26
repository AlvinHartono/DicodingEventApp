package com.example.dicodingeventapp.ui.ui.search

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

class SearchViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _searchedEvents = MutableLiveData<Result<List<Event>>>()
    val searchedEvents: LiveData<Result<List<Event>>> = _searchedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchSearchResult(query: String) {
        viewModelScope.launch(Dispatchers.Main) {
            Log.d("SearchViewModel", "Fetching search result for query: $query")

            _searchedEvents.postValue(Result.Loading)
            _isLoading.postValue(true)

            try {
                Log.d("SearchViewModel", "Fetching search result for query: $query")
                val response = eventRepository.fetchSearchResult(query)
                Log.d("SearchViewModel", "Search result fetched: ${response.value}")
                when (response.value) {
                    is Result.Error -> {
                        Log.e("SearchViewModel", "Error fetching search result: ${response.value}")

                        _searchedEvents.postValue(Result.Error("No events found"))
                        _isLoading.postValue(false)
                    }

                    Result.Loading -> {
                        Log.d("SearchViewModel", "Loading...")
                    }

                    is Result.Success -> {
                        _searchedEvents.postValue(response.value)
                        _isLoading.postValue(false)
                        Log.d("SearchViewModel", "Success fetching search result: ${response.value}")
                    }

                    null -> {
                        _searchedEvents.postValue(Result.Error("No events found"))
                        _isLoading.postValue(false)
                        Log.d("SearchViewModel", "No events found")
                    }
                }
            } catch (e: Exception) {
                _searchedEvents.postValue(Result.Error(e.message.toString()))
                _isLoading.postValue(false)
                Log.e("SearchViewModel", "Error fetching search result: ${e.message}")

            }
        }
    }
}