package com.example.dicodingeventapp.ui.ui.home

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

class HomeViewModel : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _upcomingEventsHome = MutableLiveData<List<ListEventsItem?>>()
    val upcomingEventsHome: LiveData<List<ListEventsItem?>> = _upcomingEventsHome

    private val _finishedEventsHome = MutableLiveData<List<ListEventsItem?>>()
    val finishedEventsHome: LiveData<List<ListEventsItem?>> = _finishedEventsHome

    init {
        fetchUpcomingAndFinishedEvents()
    }

    private fun fetchUpcomingAndFinishedEvents() {
        // Check if data is already fetched
        if (_upcomingEventsHome.value != null || _finishedEventsHome.value != null) {
            return
        } else {
            // Fetch data from API
            _isLoading.value = true
            val clientUpcoming =
                ApiConfig.getApiService().findEvents(active = 1, keyword = "", limit = 5)
            val clientFinished =
                ApiConfig.getApiService().findEvents(active = 0, keyword = "", limit = 5)

            // Fetch upcoming events
            clientUpcoming.enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _upcomingEventsHome.value = response.body()!!.listEvents!!
                        }
                    } else {
                        Log.d(TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
            // Fetch finished events
            clientFinished.enqueue(object : Callback<EventResponse> {
                override fun onResponse(
                    call: Call<EventResponse>,
                    response: Response<EventResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _finishedEventsHome.value = response.body()!!.listEvents!!
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
}