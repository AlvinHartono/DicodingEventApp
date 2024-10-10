package com.example.dicodingeventapp.ui.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.response.EventResponse
import com.example.dicodingeventapp.data.response.ListEventsItem
import com.example.dicodingeventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    companion object {
        private const val TAG = "SearchViewModel"
    }

    private val _searchedEvents = MutableLiveData<List<ListEventsItem?>>()
    val searchedEvents: LiveData<List<ListEventsItem?>> = _searchedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun fetchSearchedEvents(query: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().findEvents(-1, query)
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _searchedEvents.value = response.body()!!.listEvents!!
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}