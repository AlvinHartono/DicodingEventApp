package com.example.dicodingeventapp.ui.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UpcomingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Upcoming Fragment"
    }
    val text: LiveData<String> = _text
}