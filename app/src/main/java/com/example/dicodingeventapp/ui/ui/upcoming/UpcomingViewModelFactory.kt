package com.example.dicodingeventapp.ui.ui.upcoming

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.data.di.Injection
import com.example.dicodingeventapp.repository.EventRepository

class UpcomingViewModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: UpcomingViewModelFactory? = null
        fun getInstance(context: Context): UpcomingViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: UpcomingViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}