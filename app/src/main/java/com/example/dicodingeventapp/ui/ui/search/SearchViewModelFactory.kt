package com.example.dicodingeventapp.ui.ui.search

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingeventapp.data.di.Injection
import com.example.dicodingeventapp.repository.EventRepository


class SearchViewModelFactory private constructor(private val eventRepository: EventRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(eventRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: SearchViewModelFactory? = null
        fun getInstance(context: Context): SearchViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: SearchViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}