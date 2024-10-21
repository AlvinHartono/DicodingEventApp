package com.example.dicodingeventapp.ui.detail_event

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.data.di.Injection
import com.example.dicodingeventapp.repository.EventRepository

class DetailEventActivityViewModelFactory(private val eventRepository: EventRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailEventActivityViewModel::class.java)) {
            return DetailEventActivityViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
    companion object {
        @Volatile
        private var instance: DetailEventActivityViewModelFactory? = null
        fun getInstance(context: Context): DetailEventActivityViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailEventActivityViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }

}
