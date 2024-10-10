package com.example.dicodingeventapp.ui.detail_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailEventActivityViewModelFactory(private val eventId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailEventActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailEventActivityViewModel(eventId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
