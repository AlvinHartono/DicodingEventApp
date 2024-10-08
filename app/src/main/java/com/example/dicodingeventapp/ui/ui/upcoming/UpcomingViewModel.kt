package com.example.dicodingeventapp.ui.ui.upcoming

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

class UpcomingViewModel : ViewModel() {
    companion object {
        private const val TAG = "UpcomingViewModel"
    }

    private val _upcomingEvents = MutableLiveData<List<ListEventsItem?>>()
    val upcomingEvents: LiveData<List<ListEventsItem?>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents("0")
        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _upcomingEvents.value = response.body()!!.listEvents!!
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