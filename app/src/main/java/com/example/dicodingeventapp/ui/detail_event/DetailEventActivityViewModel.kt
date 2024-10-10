package com.example.dicodingeventapp.ui.detail_event

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingeventapp.data.response.DetailEventResponse
import com.example.dicodingeventapp.data.response.Event
import com.example.dicodingeventapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventActivityViewModel(private val eventId: Int) :
    ViewModel() {
    companion object {
        private const val TAG = "DetailEvtActVM"
    }

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        fetchEventDetails()
    }

    private fun fetchEventDetails() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailEvent(eventId)
        client.enqueue(object : Callback<DetailEventResponse> {
            override fun onResponse(
                call: Call<DetailEventResponse>,
                response: Response<DetailEventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()

                    responseBody?.event?.let { event -> _event.postValue(event) } ?: run {
                        Log.d(TAG, "onFailure: ${response.message()}")
                    }
                } else {
                    Log.d(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
}